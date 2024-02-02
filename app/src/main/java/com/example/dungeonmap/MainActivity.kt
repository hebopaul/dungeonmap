package com.example.dungeonmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dungeonmap.composables.TerrainScreen
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.ui.theme.DungeonMapTheme


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DungeonMapTheme {
                val fileHandler = FileHandler(this@MainActivity)
                val viewModelFactory = MainViewModelFactory( fileHandler )
                val mVM = viewModel<MainViewModel>(factory = viewModelFactory)

                DungeonMapApp(mVM)

            }
        }

    }


}


@Composable
fun DungeonMapApp(mVM: MainViewModel) {
    TerrainScreen(mVM)
}
