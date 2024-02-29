package com.example.dungeonmap.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.network.Player
import com.example.dungeonmap.utilities.toDp
import kotlinx.coroutines.launch

//This is basically the main surface of the app
// i.e the image that acts as the playing board of the game
@Composable
fun ActiveMap(
    myModifier: Modifier,
    mVM: MainViewModel,
) {
    val map = mVM.backgroundMap
    val effectsList = mVM.visibleEffects.collectAsState(initial = null).value

    //The way the ModalNavigationDrawer is build forces us to put all of the app's content
    //inside it. So everything from the active map to the buttons are in the drawer's content.
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var userNameField by remember {mutableStateOf("")}
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(700.toDp)
            ) {

                Spacer(modifier = Modifier.height(30.toDp))
                DrawerDivider(header = "You")
                PlayerRow(mVM.localPlayer)
                DrawerDivider(header = "Others")
                mVM.players?.let {
                    it.forEach { player ->
                        PlayerRow(player)
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.toDp)
                                .alpha(0.2f),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    OutlinedTextField(
                        modifier = Modifier.padding(25.toDp),
                        value = userNameField,
                        onValueChange = {
                            userNameField = it
                            mVM.updateUserName(userNameField)
                        },
                        label = {Text("Your name:")}
                        )
                }
            }
        },
        drawerState = drawerState

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),

        ) {
            if (map.uri == null)
                Image(
                    //We use the connectedModifier that was declared inside the TerrainScreen() Composable
                    modifier = myModifier,
                    contentDescription = "Stock Image",
                    painter = painterResource(
                        id = map.resId!!
                    )
                )
            else AsyncImage(
                model = map.uri!!,
                contentDescription = "Image from gallery",
                modifier = myModifier
            )


            if (mVM.effectCreatorIsVisible) EffectCreator(mVM)
        }
        
        //Visible Effects
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .scale(mVM.globalScale)
        ) { effectsList?.forEach { effect -> effect.draw()() } }
        
        //All of our active Tokens 
        Box(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(mVM.globalScale)
                    .align(Alignment.TopStart)
            ) { effectsList?.forEach { effect -> effect.draw()() } }
            Tokens(
                mVM,
                Modifier.align(Alignment.Center))
        }


        // Bottom buttons (add stuff, roll for initiative, and effects)
        TerrainUIOverlay(mVM)

        //Drawer Icon
        val drawerIconSize = 120.toDp
        Surface(
            modifier = Modifier
                .size(drawerIconSize)
                .clip(CircleShape)
                .alpha(0.3f),
            color = Color.Black
        ){}
        IconButton(
            modifier = Modifier.size(drawerIconSize),
            onClick = { scope.launch { drawerState.open() } }
        ) {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = "menu",
                tint = Color.White
            )
        }

    }
}

@Composable
fun DrawerDivider(header: String) {
    Row (
        modifier = Modifier.padding(horizontal = 20.toDp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(header)
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.toDp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun PlayerRow(player: Player) {
    Row(
        modifier = Modifier
            .height(100.toDp)
            .padding(horizontal = 30.toDp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = BiasAlignment.Vertical(0.2F)
    ){
        Surface(
            modifier = Modifier
                .size(70.toDp)
                .clip(RoundedCornerShape(10.toDp)),
            color = player.color
        ){}
        Spacer ( Modifier.width(100.toDp) )
        Text(player.userName)
        if (player.isHost) Icon(Icons.Filled.Android, "isDm", Modifier.size(20.toDp))
        Spacer ( Modifier.width(30.toDp) )
        Surface(
            modifier = Modifier
                .size(20.toDp)
                .clip(CircleShape),
            color = if (player.isConnected) Color.Green
            else if (!player.isConnected) Color.Red
            else Color.Gray
        ){}

    }
}
