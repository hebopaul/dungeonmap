package com.example.dungeonmap

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.StockImage
import com.example.dungeonmap.data.Token
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.utilities.getDrawableResourcesIds
import com.example.dungeonmap.utilities.getStockImageList
import java.util.UUID
import kotlin.random.Random

const val MIN_SCALE: Float = 1F
const val MAX_SCALE: Float = 10F



class MainViewModel(val fileHandler: FileHandler) : ViewModel() {

    //This is where we keep our global variables
    var globalPosition by mutableStateOf(Offset(0F, 0F))
        private set
    var globalScale by mutableStateOf(1F)
        private set
    var isPickerVisible by mutableStateOf(false)


    
    //Loading all of the image resources we are going to need
    private val stockD20List: List<Int> = getDrawableResourcesIds("d20")
    val stockMapsList: List<StockImage> = getStockImageList("map")
    val stockTokensList: List<StockImage> = getStockImageList("token")
    var userAddedMapsList by mutableStateOf(fileHandler.getInternalStorageMapList())
        private set
    var userAddedTokensList by mutableStateOf(fileHandler.getInternalStorageTokenList())
        private set
    var backgroundMap by mutableStateOf(BackgroundMap())
        private set
    var activeTokenList by mutableStateOf(listOf(Token()))
        private set
    
    val randomD20 = stockD20List[Random.nextInt(stockD20List.size-1)]



    //Functions to update the BackgroundMap and Token position
    fun updateMapOffset(newOffset: Offset) {
        globalPosition = Offset(newOffset.x, newOffset.y)

        activeTokenList = activeTokenList.mapIndexed { i, it ->
            it.copy(
                position = Offset(
                    activeTokenList[i].position.x + newOffset.x,
                    activeTokenList[i].position.y + newOffset.y
                )
            )
        }
    }
    

    //This function is called when the user pinches to zoom in or out
    fun updateMapScale(scaleChange: Float) {

        if (scaleChange != 0F) {

            globalScale = scaleChange * globalScale.coerceIn(
                                                        MIN_SCALE,
                                                        MAX_SCALE
                                                    )
            activeTokenList = activeTokenList.mapIndexed { i, it ->
                it.copy(
                    scale = scaleChange * activeTokenList[i].scale.coerceIn(MIN_SCALE, MAX_SCALE)
                )
            }
            //In order for the map to stay centered on the screen when zooming in or out, we need to
            //update the map offset as well
            if (globalScale < MAX_SCALE && globalScale > MIN_SCALE)
                globalPosition = Offset(
                    globalPosition.x * scaleChange,
                    globalPosition.y * scaleChange
                )

            //We also need to update the token offset so that it stays in the same position on the map
            if (globalScale < MAX_SCALE && globalScale > MIN_SCALE)
                activeTokenList = activeTokenList.mapIndexed { i, it ->
                    it.copy(
                        position = Offset(
                            activeTokenList[i].position.x * scaleChange,
                            activeTokenList[i].position.y * scaleChange
                        )
                    )
                }
        }

        Log.d(
            "updateMapScale called",
            "map offset = ${globalPosition}" +
            "map scale = ${globalScale}" +
            "token offset = ${activeTokenList[0].position}" +
            "token scale = ${activeTokenList[0].scale}"
        )
    }

    //This function is called when the user drags the token
    fun updateTokenOffset(newPosition: Offset, uuid: UUID) {

        activeTokenList = activeTokenList.mapIndexed { i, it ->
            if (it.uuid == uuid)
                it.copy(
                    position = Offset(
                        activeTokenList[i].position.x + newPosition.x *
                                globalScale * activeTokenList[i].tokenSize,
                        activeTokenList[i].position.y + newPosition.y *
                                globalScale * activeTokenList[i].tokenSize
                     )
                )
            else it
        }
    }


    fun updateTokenSize(sizeChange: Float, uuid: UUID) {
        activeTokenList = activeTokenList.mapIndexed { i, it ->
            if (it.uuid == uuid)
                it.copy(
                    tokenSize = (activeTokenList[i].tokenSize * ( sizeChange ).coerceIn(0.04F, 2F))
                )
            else it
        }
    }

    fun createToken(drawable: Int? = null) {

        if (activeTokenList.isNotEmpty()){
            activeTokenList += mutableListOf(
                Token(
                    drawableRes = drawable ?: R.drawable.minotaur_berserker,
                    tokenSize = activeTokenList.last().tokenSize,
                    scale = activeTokenList.last().scale,
                    position = (activeTokenList.last().position + Offset(10f, 10f))
                )
            )
        } else activeTokenList = mutableListOf(Token(drawable!!))

    }

    fun deleteToken(uuid: UUID) {
        activeTokenList = activeTokenList.filter { it.uuid!= uuid }
    }

    fun duplicateToken(token: Token) {
        createToken(token.drawableRes)
    }

    fun setTokenInitiative (number: Int, uuid: UUID) {
        activeTokenList = activeTokenList.mapIndexed { index, token ->
            if (token.uuid == uuid)
                token.copy(currentInitiave = number)
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
            isPickerVisible = state
    }

    fun updateUserAddedMapsList(){
        userAddedMapsList = fileHandler.getInternalStorageMapList()
    }
    fun updateUserAddedTokensList(){
        userAddedTokensList = fileHandler.getInternalStorageTokenList()
    }

    fun makeMapSelected () {
        backgroundMap = backgroundMap.copy(
            isSelected = true
        )
        activeTokenList = activeTokenList.map {
            it.copy(
                isSelected = false
            )
        }
    }

    fun makeTokenSelected (selectedToken: Token) {
        activeTokenList = activeTokenList.mapIndexed { index, token ->
            if (token.uuid == selectedToken.uuid)
                token.copy(
                    isSelected = true
                )
            else
                token.copy(
                    isSelected = false
                )
        }
        backgroundMap = backgroundMap.copy(
            isSelected = false
        )
    }

    fun getSelectedTokenUuid (): UUID {
        return activeTokenList.find { it.isSelected }?.uuid?: UUID.randomUUID()
    }

    fun everyoneRollForInitiative () {
        activeTokenList.map {
            it.copy(
                 currentInitiave = Random.nextInt(1, 20) + it.initiativeModifier
            )
        }
        activeTokenList.forEach {
            Log.d("Initiative", "${it.name}: ${it.currentInitiave}")
        }
    }

}
