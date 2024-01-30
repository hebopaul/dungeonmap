package com.example.dungeonmap.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.utilities.toDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokenPickerTab( mVM: MainViewModel) {

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .border(
                3.dp,
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(10.dp)
            ),
        shadowElevation = 15.dp,
        color = MaterialTheme.colorScheme.secondary

    ){
        Column {
            Spacer(modifier = Modifier.height(30.toDp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
            ) {
                mVM.stockTokensList.forEach { token ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.toDp, vertical = 10.toDp)
                        ){
                            Icon(
                                painter = token!!.image,
                                contentDescription = token.name,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(250.toDp)
                                    .clickable {
                                        mVM.createToken(token.id)
                                        mVM.setPickerVisible(false)
                                    }
                            )
                            Spacer(Modifier.width(30.toDp))
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.CenterVertically),
                                shadowElevation = 5.dp,
                                //color = MaterialTheme.colorScheme.background
                            ) {
                                Text( token.name)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(120.toDp))
        }
    }
}


