package com.example.dungeonmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dungeonmap.composables.TerrainScreen
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.ui.theme.DungeonMapTheme
import com.example.dungeonmap.utilities.getDrawableResourcesIds


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DungeonMapTheme {
                val viewModelFactory = MainViewModelFactory()
                val mVM = viewModel<MainViewModel>(factory = viewModelFactory)

                val fileHandler = FileHandler(this@MainActivity)

                mVM.stockTokensList = getDrawableResourcesIds("token")
                mVM.stockMapsList = getDrawableResourcesIds("map")

                mVM.userAddedTokensList = fileHandler.getInternalTokenList()
                mVM.userAddedMapsList = fileHandler.getInternalMapList()



                DungeonMapApp(mVM, fileHandler)

            }
        }

    }


}


@Composable
fun DungeonMapApp(mVM: MainViewModel, fileHandler: FileHandler) {
    TerrainScreen(mVM, fileHandler)
}






//This composable creates an animated tab that rises from below the screen, that presents the user with a
//lazy grid of all the drawable that are returned by getDrawableResourcesIds() function







