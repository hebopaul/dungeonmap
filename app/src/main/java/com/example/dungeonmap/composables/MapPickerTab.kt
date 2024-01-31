package com.example.dungeonmap.composables

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.dungeonmap.MainActivity.Companion.fileHandler
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.data.StockImage
import com.example.dungeonmap.ui.theme.arsenalFamily
import com.example.dungeonmap.utilities.toDp


@Composable
fun MapPickerTab(
    mVM: MainViewModel
) {

    val mapPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { fileHandler.importMapFromDevice(it)}
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { mVM.setPickerVisible(false) },
        contentAlignment = Alignment.BottomCenter

    )   {

        Surface (
            modifier = Modifier
                .fillMaxSize(),
            shadowElevation = 15.dp,
            color = MaterialTheme.colorScheme.background

        ){
            CollapsibleContent(header = "Stock"){
                LazyMapList(mVM, mapPickerLauncher)
            }
        }
    }
}

@Composable
fun LazyMapList(
    mVM: MainViewModel,
    mapPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 5.dp, end = 5.dp, top = 5.toDp, bottom = 5.toDp),
        modifier = Modifier
            .fillMaxSize(),
    )
    {
        mVM.stockMapsList.forEachIndexed { index, map ->
            item {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.toDp)
                        .padding(vertical = 6.toDp),
                    shadowElevation = 3.dp,
                    color = MaterialTheme.colorScheme.background
                ){
                    MapItemRow(mVM, index, map, mapPickerLauncher)

                }
            }
        }
    }
}


@Composable
fun MapItemRow(
    mVM: MainViewModel,
    index: Int,
    map: StockImage?,
    mapPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.toDp, vertical = 10.toDp)
            .clickable {
                mVM.updateMapImageResource(map!!.id)
                mVM.setPickerVisible(false)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (index !== mVM.stockMapsList.size - 1)
            Arrangement.Start
        else Arrangement.Center
    ){
        if (index !== mVM.stockMapsList.size - 1){
            Icon(
                painter = map!!.image,
                contentDescription = map.name,
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxHeight(0.97F)
                    .padding(start = 15.toDp)
            )
            Spacer(Modifier.width(30.toDp))

            Text(
                text = map.name ,
                fontSize = 20.sp,
                fontFamily = arsenalFamily
            )
        }
        else {
            IconButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .scale(1.7F),
                onClick = {
                    mapPickerLauncher.launch(
                        PickVisualMediaRequest( ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Import",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
