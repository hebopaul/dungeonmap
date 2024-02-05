package com.example.dungeonmap.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.data.InternalStorageImage
import com.example.dungeonmap.data.StockImage
import com.example.dungeonmap.ui.theme.arsenalFamily
import com.example.dungeonmap.utilities.beautifyResName
import com.example.dungeonmap.utilities.toDp


@Composable
fun TokenPickerTab(
    mVM: MainViewModel
) {

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

    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()

        ) {

            item {
                CollapsibleContent(header = "Stock") {
                    TokenList(mVM)
                }
            }
            item {
                CollapsibleContent(header = "User Added") {
                    TokenList(mVM, true)
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.toDp))
                ImportItemIcon(tokenPickerLauncher)
                Spacer(modifier = Modifier.height(100.toDp))
            }

        }
    }

}

@Composable
fun TokenList(
    mVM: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ){
        mVM.stockTokensList.forEach{ token ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.toDp)
                    .padding(vertical = 6.toDp),
                shadowElevation = 3.dp,
                color = MaterialTheme.colorScheme.background
            ){
                TokenRowItem(mVM, token )
            }
        }
    }
}

@Composable
fun TokenList(
    mVM: MainViewModel,
    isUserAdded: Boolean = false
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        mVM.userAddedTokensList.forEach { token ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.toDp)
                    .padding(vertical = 6.toDp),
                shadowElevation = 3.dp,
                color = MaterialTheme.colorScheme.background
            ) {
                TokenRowItem(mVM, token)
            }
        }
    }
}



@Composable
fun TokenRowItem(
    mVM: MainViewModel,
    token: StockImage?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.toDp, vertical = 10.toDp)
            .clickable {
                mVM.createToken(token!!.id)
                mVM.setPickerVisibility(false)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,

        ){

        Icon(
            painter = painterResource(token!!.id),
            contentDescription = token.name,
            tint = Color.Unspecified,
            modifier = Modifier
                .fillMaxHeight(0.97F)
                .padding(start = 15.toDp)
        )
        Spacer(Modifier.width(30.toDp))

        Text(
            text = token.name ,
            fontSize = 20.sp,
            fontFamily = arsenalFamily
        )
    }
}

@Composable
fun TokenRowItem(
    mVM: MainViewModel,
    token: InternalStorageImage?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.toDp, vertical = 10.toDp)
            .clickable {
                //TODO: mVM.createToken(token.image)
                mVM.setPickerVisibility(false)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ){

        Image(
            painter = token!!.painter,
            contentDescription = token.name,
            modifier = Modifier
                .fillMaxHeight(0.97F)
                .padding(start = 15.toDp)
        )
        Spacer(Modifier.width(30.toDp))

        Text(
            text = beautifyResName(token.name) ,
            fontSize = 20.sp,
            fontFamily = arsenalFamily
        )

        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete file",
            modifier = Modifier
                .scale(1.5f)
                .clickable {
                    mVM.fileHandler.deleteImageFromInternalStorage(token.uri)
                    mVM.updateUserAddedTokensList()
                }
        )
    }
}
