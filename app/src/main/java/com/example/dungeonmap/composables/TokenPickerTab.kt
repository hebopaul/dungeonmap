package com.example.dungeonmap.composables

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
fun TokenPickerTab(
    mVM: MainViewModel
) {

    val tokenPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { fileHandler.importTokenFromDevice(it) }
    )

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background

    ) {
        Column{
            CollapsibleContent(header = "Stock") {
                LazyTokenList(mVM, tokenPickerLauncher)
            }

        }

    }
}

@Composable
fun LazyTokenList(
    mVM: MainViewModel,
    tokenPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.toDp),
    ) {
        mVM.stockTokensList.forEachIndexed { index, token ->
            item {
                TokenRowItem(
                    mVM,
                    tokenPickerLauncher,
                    index,
                    token
                )
            }
        }
    }
}

@Composable
fun TokenRowItem(
    mVM: MainViewModel,
    tokenPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    index: Int,
    token: StockImage?
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
                .clickable {
                    mVM.updateMapImageResource(token!!.id)
                    mVM.setPickerVisible(false)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (index !== mVM.stockTokensList.size - 1)
                Arrangement.Start
            else Arrangement.Center
        ) {
            if (index !== mVM.stockTokensList.size - 1) {
                Icon(
                    painter = token!!.image,
                    contentDescription = token.name,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxHeight(0.97F)
                        .padding(start = 15.toDp)
                )
                Spacer(Modifier.width(30.toDp))

                Text(
                    text = token.name,
                    fontSize = 20.sp,
                    fontFamily = arsenalFamily
                )
            } else {
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .scale(1.7F),
                    onClick = {
                        tokenPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
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

}






