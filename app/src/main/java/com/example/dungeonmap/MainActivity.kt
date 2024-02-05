package com.example.dungeonmap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.ui.theme.DungeonMapTheme
import com.example.dungeonmap.utilities.toDp

//This is a complete redux of the app... ongoing
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            DungeonMapTheme {

                val fileHandler = FileHandler(this@MainActivity)
                val mVM = viewModel<MainViewModel>(
                    factory = MainViewModelFactory( fileHandler )
                )

                mVM.addToken(R.drawable.contemplative_woman_of_the_wilds)

                var globalScale by remember { mutableFloatStateOf(1F) }
                var globalPosition by remember { mutableStateOf ( Offset(0F, 0F))}

                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    content = {}
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = if (mVM.activeMap.resId != null) mVM.activeMap.resId
                        else mVM.activeMap.uri,
                        contentDescription = "Active Map",
                        modifier = Modifier
                            .scale(globalScale)
                            .offset(globalPosition.x.toDp, globalPosition.y.toDp)

                            .pointerInput(Unit) {
                                detectTransformGestures { _, drag, scale, _ ->
                                    Log.d("Transform Gesture", "drag = $drag, scale = $scale")
                                    if (scale == 1F) globalPosition += drag
                                    globalScale *= scale
                                    Log.d(
                                        "Global",
                                        "globalPosition = $globalPosition, globalScale = $globalScale"
                                    )
                                }
                            }
                    )

                    Icon(
                        painter = painterResource(R.drawable.token__arkhan_the_cruel),
                        contentDescription = "Token",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .scale(globalScale * mVM.activeTokens.value[0].size)
                            .offset (
                                x = (
                                    (globalPosition.x + mVM.activeTokens.value[0].position.x) 
                                ).toDp,
                                y = (
                                    (globalPosition.y + mVM.activeTokens.value[0].position.y)
                                ).toDp
                            )

                            .pointerInput(Unit) {
                                detectTransformGestures { _, drag, scale, _ ->
                                    mVM.updateTokenPosition(mVM.activeTokens.value[0].uuid, drag)
                                    Log.d("token position", "On viewModel: ${mVM.activeTokens.value[0].position}")
                                }
                            }

                    )
                }






            }
        }
    }

}


