package com.example.dungeonmap.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.data.ImageRepresentable
import com.example.dungeonmap.ui.theme.arsenalFamily
import com.example.dungeonmap.utilities.beautifyResName
import com.example.dungeonmap.utilities.launchPhotos
import com.example.dungeonmap.utilities.toDp


@Composable
fun TokenPickerTab(mVM: MainViewModel) {

    val tokenPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            mVM.fileHandler.importTokenFromDevice(it)
            mVM.updateUserAddedTokensList()
        }
    )

    Surface (
        modifier = Modifier
            .fillMaxSize(),
        shadowElevation = 15.dp,
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()

        ) {

            item {
                CollapsibleContent(header = "Stock") {
                    // NK: This generalization could be
                    // propagated into the CollapsibleContent
                    // to create a CollapsibleList that gets
                    // title, items, and itemView
                    TokenList(items = mVM.stockTokensList, onDelete = null) {
                        mVM.createToken(it.id)
                        mVM.setPickerVisible(false)
                    }
                }
            }
            item {
                CollapsibleContent(header = "User Added") {
                    TokenList(
                        items = mVM.userAddedTokensList,
                        onDelete = {
                            mVM.fileHandler.deleteImageFromInternalStorage(it.uri)
                            mVM.updateUserAddedTokensList()
                        },
                        onClick = {
                            mVM.setPickerVisible(false)
                        })
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.toDp))
                ImportItemIcon(tokenPickerLauncher::launchPhotos)
                Spacer(modifier = Modifier.height(100.toDp))
            }

        }
    }

}

@Composable
fun <ImageType: ImageRepresentable> TokenList(
    items: List<ImageType>,
    onDelete : ((ImageType) -> Unit)?,
    onClick  :  (ImageType) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        items.forEach { token ->
            TokenRowItem(
                token = token,
                onDelete = onDelete?.let { { it.invoke(token) } },
                onClick =  { onClick(token) }
            )
        }
    }
}

@Composable
fun ColumnScope.TokenRowItem(
    token: ImageRepresentable, // NK: The original was nullable. Why?
    onDelete: (() -> Unit)? = null,
    onClick: () -> Unit,
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
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Image(
                painter = token.painter,
                contentDescription = token.name,
                modifier = Modifier
                    .fillMaxHeight(0.97F)
                    .padding(start = 15.toDp)
            )
            Spacer(Modifier.width(30.toDp))

            Text(
                text = beautifyResName(token.name),
                fontSize = 20.sp,
                fontFamily = arsenalFamily
            )

            onDelete?.let { deleteAction ->
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete file",
                    modifier = Modifier
                        .scale(1.5f) // NK: Maybe with size modifier?
                        .clickable(onClick = deleteAction)
                )
            }
        }
    }
}
