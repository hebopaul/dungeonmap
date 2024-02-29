package com.example.dungeonmap

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dungeonmap.data.AnimatedPointer
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.Circle
import com.example.dungeonmap.data.InternalStorageImage
import com.example.dungeonmap.data.Line
import com.example.dungeonmap.data.Polygon
import com.example.dungeonmap.data.Rectangle
import com.example.dungeonmap.data.StockImage
import com.example.dungeonmap.data.Token
import com.example.dungeonmap.data.VisibleEffect
import com.example.dungeonmap.network.NetworkState
import com.example.dungeonmap.network.Player
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.utilities.getDrawableResourcesIds
import com.example.dungeonmap.utilities.getNameFromResId
import com.example.dungeonmap.utilities.getStockImageList
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import kotlinx.coroutines.launch
import me.nikhilchaudhari.quarks.BuildConfig
import java.util.UUID
import kotlin.random.Random

const val MIN_SCALE: Float = 1F
const val MAX_SCALE: Float = 10F
const val X_BOUNDARY: Float = 350F
const val Y_BOUNDARY: Float = 500F

class MainViewModel(
    val fileHandler: FileHandler,
    val connectionsClient: ConnectionsClient
) : ViewModel() {


    var userAddedMapsList: List<InternalStorageImage> by mutableStateOf(listOf())
        private set
    var userAddedTokensList: List<InternalStorageImage> by mutableStateOf(listOf())
        private set
    init {
        viewModelScope.launch{
            userAddedMapsList = fileHandler.getInternalStorageMapList()
            userAddedTokensList = fileHandler.getInternalStorageTokenList()
        }
    }

    //Loading all of the image resources we are going to need
    val stockMapsList: List<StockImage> = getStockImageList("map")
    private val stockD20List: List<Int> = getDrawableResourcesIds("d20")
    val stockTokensList: List<StockImage> = getStockImageList("token")


    //This is where we keep our global variables
    var globalPosition by mutableStateOf(Position(0F, 0F))
        private set
    var globalScale by mutableFloatStateOf(1F)
        private set

    var pickerIsVisible by mutableStateOf(false)
    var effectCreatorIsVisible by mutableStateOf(false)
    var uiIsVisible by mutableStateOf(true)




    var backgroundMap by mutableStateOf(BackgroundMap())
        private set
    var _activeTokens: List<Token> by mutableStateOf(listOf())
        private set
    var battleOngoing by mutableStateOf(false)
        private set
    var currentToken by mutableStateOf(0)
        private set
    var _visibleEffects: List<VisibleEffect> by mutableStateOf(listOf(Circle()))
        private set

    val visibleEffects = snapshotFlow {
        _visibleEffects.map { effect ->
            when (effect) {
                is Circle -> effect.copy(globalPosition / globalScale + (effect.position * globalScale))
                is Rectangle -> effect.copy(globalPosition / globalScale + (effect.position * globalScale))
                is Polygon -> effect.copy(globalPosition / globalScale + (effect.position * globalScale))
                is Line -> effect.copy(globalPosition / globalScale + (effect.position * globalScale))
            }
        }
    }
    var tempPointerEffect: AnimatedPointer? by mutableStateOf(null)
    val activeTokens = snapshotFlow{
        _activeTokens.map { token -> token.copy(position = globalPosition + (token.position * globalScale))}
    }

    val randomD20 = stockD20List[Random.nextInt(stockD20List.size-1)]


    fun updateGlobalPosition(newOffset: Offset) {
        globalPosition += newOffset
    }

    //This function is called when the user pinches to zoom in or out
    fun updateGlobalScale(scaleChange: Float) {
        globalScale = (globalScale * scaleChange).coerceIn(MIN_SCALE, MAX_SCALE)
        globalPosition = (globalPosition * scaleChange)
        _visibleEffects.forEach { if (MAX_SCALE > globalScale && MIN_SCALE < globalScale)it.position /= scaleChange }
        if(_activeTokens.isNotEmpty())
            Log.d("Moved token","globalScale:${globalScale} "+"global: $globalPosition "+"token: ${_activeTokens[0].position}")
    }

    //This function is called when the user drags the token
    fun updateTokenPosition(newPosition: Offset, uuid: UUID) {
        Log.d("Moved token","globalScale:${globalScale} "+"global: ${globalPosition} "+"token: ${_activeTokens[0].position}")
        _activeTokens = _activeTokens.map { token ->
            if (token.uuid == uuid) token.copy(
                    position = token.position + newPosition / globalScale
                )
            else token
        }
    }

    fun updateTokenSize(sizeChange: Float, uuid: UUID) {
        _activeTokens = _activeTokens.mapIndexed { i, it ->
            if (it.uuid == uuid) {
                Log.d("updateTokenSize", "token size:${it.size}")
                it.copy( size = (_activeTokens[i].size * (sizeChange).coerceIn(0.04F, 2F)) )
            }
            else it
        }
    }

    fun createToken(drawable: Int? = null) {
        if (_activeTokens.isNotEmpty() ) { _activeTokens += mutableListOf(
                Token(
                    drawableRes = drawable ?: R.drawable.minotaur_berserker,
                    size = _activeTokens.last().size,
                    position = (_activeTokens.last().position + Offset(10f, 10f)),
                    name = getNameFromResId(drawable!!)
                )
            )
        } else _activeTokens = mutableListOf(Token(drawable!!))
    }

    fun deleteToken(uuid: UUID) {
        _activeTokens = _activeTokens.filter { it.uuid!= uuid }
    }

    fun duplicateToken(token: Token) {
        createToken(token.drawableRes)
    }

    fun setTokenInitiative (number: Int, uuid: UUID) {
        _activeTokens = _activeTokens.mapIndexed { index, token ->
            if (token.uuid == uuid)
                token.copy(currentInitiative = number)
            else
                token
        }
    }

    fun updateMapImageUri (uri: Uri? ) {
        backgroundMap.uri = uri
        backgroundMap.resId = null

    }

    fun updateMapResourceId (resource: Int) {
        backgroundMap.resId =  resource
        backgroundMap.uri = null
    }

    fun setPickerVisibility (state: Boolean) {
            pickerIsVisible = state
    }

    suspend fun updateUserAddedMapsList(){
        userAddedMapsList = fileHandler.getInternalStorageMapList()
    }
    suspend fun updateUserAddedTokensList(){
        userAddedTokensList = fileHandler.getInternalStorageTokenList()
    }

    fun makeMapSelected () {
        backgroundMap = backgroundMap.copy( isSelected = true )
        _activeTokens = _activeTokens.map { it.copy( isSelected = false ) }
        if(_activeTokens.isNotEmpty())
            Log.d("Selection", "isMapSelected: ${backgroundMap.isSelected}, isTokenSelected: ${_activeTokens[0].isSelected}")
    }

    fun makeTokenSelected (selectedToken: Token) {
        _activeTokens = _activeTokens.mapIndexed { index, token ->
            if (token.uuid == selectedToken.uuid) token.copy( isSelected = true )
            else token.copy( isSelected = false )
        }
        backgroundMap = backgroundMap.copy( isSelected = false )
        if(_activeTokens.isNotEmpty())
            Log.d("Selection", "isMapSelected: ${backgroundMap.isSelected}, isTokenSelected: ${_activeTokens[0].isSelected}")
    }

    fun getSelectedTokenUuid (): UUID {
        return _activeTokens.find { it.isSelected }!!.uuid
    }

    fun rollForInitiative () {
        _activeTokens = _activeTokens.map {
            val dieCast = Random.nextInt(1, 20)
            println("Rolling for: $dieCast")
            it.copy(
                 currentInitiative =  dieCast + it.initiativeModifier
            )
        }.sortedByDescending { it.currentInitiative + (it.initiativeModifier/10F) }
        _activeTokens.forEach {
            Log.d("Initiative", "${it.name}: ${it.currentInitiative}")
        }
        //We start with the second occurrence of the token with the highest initiative
        currentToken = _activeTokens.size
        battleOngoing = true

    }

    fun nextTokenClicked (endOfList: Boolean) {
        when (endOfList) {
            false -> currentToken++
            true -> currentToken %= _activeTokens.size
        }
    }

    fun endBattle () {
        battleOngoing = false
    }

    fun previousTokenClicked(startOfList: Boolean) {
        when (startOfList) {
            false -> currentToken--
            true -> currentToken += _activeTokens.size
        }
    }

    fun addCircleEffect(position: Position, radius: Float) {
        _visibleEffects += mutableListOf( Circle(position - globalPosition, radius) )
        Log.d("Effect Placed", "at position: $position")
    }

    fun addRectangleEffect(position: Position, dimensions: Size) {
        _visibleEffects += mutableListOf( Rectangle(position - globalPosition*globalScale, dimensions/globalScale) )
        Log.d("Effect Placed", "at position: $position")
    }

    fun addPolygonEffect(position: Position, points: List<Position>) {
        _visibleEffects += mutableListOf( Polygon(position - globalPosition, points) )
        Log.d("Effect Placed", "at position: $position")
    }

    fun addLineEffect(position: Position, offset: Offset) {
        _visibleEffects += mutableListOf( Line(position - globalPosition, offset) )
        Log.d("Effect Placed", "at position: $position")
    }

    fun addAnimatedPointerEffect(position: Position, duration: Int) {
        tempPointerEffect = AnimatedPointer(position, duration)
    }
    fun animatedPointerStopped() { tempPointerEffect = null}



    //networking
    var localPlayer = Player (
        userName = "pavlos",
        color = Color.Red,
        isConnected = true
    )
    var players: List<Player>? = listOf(
        Player (
            userName = "pavlos",
            color = Color.Blue,
            isConnected = true
        ),
        Player (
            userName = "pavlos",
            color = Color.Green,
            isConnected = true
        ),
        Player (
            userName = "pavlos",
            color = Color.Yellow,
            isConnected = false
        )
    )
    fun updateUserName(userName: String){
        localPlayer = localPlayer.copy(userName = userName)
    }

    var networkState = NetworkState()

    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            Log.d("Network", "onPayloadReceived")
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Log.d("Network", "onPayloadTransferUpdate")
        }
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            Log.d("Network", "onConnectionInitiated")

            Log.d("Network", "Accepting connection...")
            connectionsClient.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
            Log.d("Network", "onConnectionResult")

            when (resolution.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> {
                    Log.d("Network", "ConnectionStatusCodes.STATUS_OK")
                    players = players?.plus(listOf( Player(endpointId = endpointId) ))
                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> Log.d("Network", "ConnectionStatusCodes.STATUS_CONNECTION_REJECTED")
                ConnectionsStatusCodes.STATUS_ERROR -> Log.d("Network", "ConnectionStatusCodes.STATUS_ERROR")
                else -> Log.d("Network", "Unknown status code: ${resolution.status.statusCode}")
            }
        }

        override fun onDisconnected(endpointId: String) {
            Log.d("Network", "onDisconnected")
        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound( endpointId: String, info: DiscoveredEndpointInfo) {
            Log.d("Network", "onEndpointFound")
            Log.d("Network", "Requesting connection...")

            connectionsClient.requestConnection(
                localPlayer.userName,
                endpointId,
                connectionLifecycleCallback
            ).addOnSuccessListener {
                Log.d("Network", "Successfully requested connection")
            }.addOnFailureListener {
                Log.d("Network", "Failed to request connection")
            }
        }

        override fun onEndpointLost( endpointId: String) {
            Log.d("Network", "onEndpointLost")
        }
    }
    fun startHosting() {
        Log.d("Network", "Start advertising...")
        networkState.isHosting = true
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build()

        connectionsClient.startAdvertising(
            localPlayer.userName,
            BuildConfig.LIBRARY_PACKAGE_NAME,
            connectionLifecycleCallback,
            advertisingOptions
        ).addOnSuccessListener {
            Log.d("Network", "Advertising...")
            localPlayer.isHost = true
        }.addOnFailureListener {
            Log.d("Network", "Failed to start hosting...")
        }
    }

    fun startDiscovering() {
        Log.d("Network", "Start discovering...")
        networkState.isDiscovering = true
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build()

        connectionsClient.startDiscovery(
            BuildConfig.LIBRARY_PACKAGE_NAME,
            endpointDiscoveryCallback,
            discoveryOptions
        ).addOnSuccessListener {
            Log.d("Network", "Discovering...")
            networkState.isDiscovering = true
        }.addOnFailureListener {
            Log.d("Network", "Failed to start discovering...")
        }
    }
}
