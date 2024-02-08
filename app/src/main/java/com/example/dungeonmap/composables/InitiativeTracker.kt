package com.example.dungeonmap.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.dungeonmap.R
import com.example.dungeonmap.utilities.toDp


@Composable
fun InitiativeTracker() {
    val  scrollState = rememberScrollState()
    Box(modifier = Modifier
        .fadingEdges(400F)
        .fillMaxWidth()
        .height(100.toDp)
    ) {
        Row( modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState)) {
                repeat(20) {
                    Icon(
                        painterResource(R.drawable.token__avacyn),
                        "Initiative Tracker Token",
                        Modifier.fillMaxHeight(),
                        Color.Unspecified
                    )
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun testInitiativeTracker() {
    InitiativeTracker()
}
