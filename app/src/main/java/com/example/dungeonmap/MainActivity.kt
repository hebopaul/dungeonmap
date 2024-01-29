package com.example.dungeonmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.dungeonmap.composables.TerrainScreen
import com.example.dungeonmap.ui.theme.DungeonMapTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DungeonMapTheme {
                DungeonMapApp()
            }
        }
    }
}


@Composable
fun DungeonMapApp() {
    TerrainScreen()
}






//This composable creates an animated tab that rises from below the screen, that presents the user with a
//lazy grid of all the drawable that are returned by getDrawableResourcesIds() function







