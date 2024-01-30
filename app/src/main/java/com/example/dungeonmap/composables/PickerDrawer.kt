@file:OptIn(ExperimentalFoundationApi::class)

package com.example.dungeonmap.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.RunCircle
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.RunCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.dungeonmap.MainViewModel

@Composable
fun PickerDrawer(mVM: MainViewModel) {
    val tabItems = listOf(
        TabItem(
            title = "Maps",
            unselectedIcon = Icons.Outlined.Map,
            selectedIcon = Icons.Filled.Map
        ),
        TabItem(
            title = "Tokens",
            unselectedIcon = Icons.Outlined.RunCircle,
            selectedIcon = Icons.Filled.RunCircle
        )
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabItems.size }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }
    Surface(color = MaterialTheme.colorScheme.background) {
        Column {

            TabRow(selectedTabIndex = selectedTabIndex) {
                tabItems.forEachIndexed { index, tabItem ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(tabItem.title) },
                        icon = {
                            Icon(
                                imageVector =
                                    if (selectedTabIndex == index) tabItem.selectedIcon
                                    else tabItem.unselectedIcon,
                                contentDescription = tabItem.title
                            )
                        }
                    )

                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
            ) {pagerIndex ->
                Box (
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (pagerIndex) {
                        0 -> MapPickerTab(mVM)
                        1 -> TokenPickerTab(mVM)
                    }
                }
            }

        }
    }
}

data class TabItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

@Preview ()
@Composable
fun ShowMeUBits() {
    PickerDrawer(MainViewModel())
}