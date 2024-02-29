package com.example.dungeonmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dungeonmap.composables.TerrainScreen
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.ui.theme.DungeonMapTheme
import com.example.dungeonmap.utilities.SCREEN_HEIGHT_DP
import com.example.dungeonmap.utilities.SCREEN_WIDTH_DP
import com.example.dungeonmap.utilities.toPxFloat
import com.google.android.gms.nearby.Nearby


class MainActivity : ComponentActivity() {

    companion object {
        val REQUIRED_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
    }

    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.any { !it.value}) {
            Toast.makeText(this, "Required permissions needed", Toast.LENGTH_LONG).show()
            finish()
        } else {
            recreate()
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.isEmpty() || permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    override fun onStart() {
        super.onStart()
        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestPermissions.launch( REQUIRED_PERMISSIONS )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DungeonMapTheme {
                val fileHandler = FileHandler(this@MainActivity)
                val viewModelFactory = MainViewModelFactory(
                    fileHandler,
                    Nearby.getConnectionsClient(applicationContext)
                )
                val mVM = viewModel<MainViewModel>(factory = viewModelFactory)

                Position.updateScreenMax(SCREEN_WIDTH_DP.toPxFloat, SCREEN_HEIGHT_DP.toPxFloat)
                println("screen max is ${Position.screenMax}")

                TerrainScreen(mVM)

            }
        }

    }


}

