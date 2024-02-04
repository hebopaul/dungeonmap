package com.example.dungeonmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.ui.theme.DungeonMapTheme

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

                    val globalScale by remember { mutableFloatStateOf(1F) }
                    val globalPosition by remember { mutableStateOf (IntOffset(0, 0))}




            }
        }
    }

}
