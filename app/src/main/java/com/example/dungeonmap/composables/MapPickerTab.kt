package com.example.dungeonmap.composables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.data.ImageRepresentable
import com.example.dungeonmap.ui.theme.arsenalFamily
import com.example.dungeonmap.utilities.beautifyResName
import com.example.dungeonmap.utilities.launchPhotos
import com.example.dungeonmap.utilities.toDp


@Composable
fun MapPickerTab(mVM: MainViewModel) {

    val mapPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            mVM.fileHandler.importMapFromDevice(it)
            mVM.updateUserAddedMapsList()
        }
    )


    Surface (
        modifier = Modifier.fillMaxSize(),
        shadowElevation = 15.dp,
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(Modifier.fillMaxSize()) {

           item {
                CollapsibleContent(header = "Stock") {
                    MapList(items = mVM.stockMapsList, onDelete = null) {
                        mVM.updateMapImageResource(it.id)
                        mVM.setPickerVisible(false)
                    }
                }
            }
           item {
                CollapsibleContent(header = "User Added") {
                    MapList(
                        items = mVM.userAddedMapsList,
                        onDelete = {
                            mVM.fileHandler.deleteImageFromInternalStorage(it.uri)
                            mVM.updateUserAddedMapsList()
                        },
                        onClick = {
                            mVM.updateMapImageUri(Uri.parse(it.uri))
                            mVM.setPickerVisible(false)
                        })
                }
            }
           item {
                Spacer(modifier = Modifier.height(100.toDp))
                ImportItemIcon(mapPickerLauncher::launchPhotos)
                Spacer(modifier = Modifier.height(100.toDp))
            }

        }
    }

}


@Composable
fun <ImageType: ImageRepresentable> MapList(
    items: List<ImageType>,
    onDelete: ((ImageType) -> Unit)?,
    onClick: (ImageType) -> Unit
) = Column(Modifier.fillMaxSize()) {
    items.forEach { map ->
        MapRowItem(
            map = map,
            onDelete = onDelete?.let { { it.invoke(map) } },
            onClick = { onClick(map) })
    }
}


@Composable
fun <ImageType: ImageRepresentable> MapRowItem(
    map: ImageType,
    onDelete: (() -> Unit)?,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.toDp)
            .padding(vertical = 6.toDp),
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.toDp, vertical = 10.toDp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            Icon(
                painter = map.painter,
                contentDescription = map.name,
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxHeight(0.97F) // NK: Could this be a top or bottom height?
                    .padding(start = 15.toDp))

            Spacer(Modifier.width(30.toDp))

            Text(
                text = beautifyResName(map.name),
                fontSize = 20.sp,
                fontFamily = arsenalFamily
            )

            onDelete?.let { deleteAction ->
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete file",
                    modifier = Modifier
                        .scale(1.5f)
                        .clickable(onClick = onDelete)
                )
            }

        }
    }
}
