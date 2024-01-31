

package com.example.dungeonmap.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.RunCircle
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.RunCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.utilities.toDp

@OptIn(ExperimentalFoundationApi::class)
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
    // The pager state, which keeps track of the current page and
    // whether or not the user is currently swiping between pages.
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

@Composable
fun CollapsibleContent(
    header: String,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(true)}

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.toDp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                modifier = Modifier
                    .fillMaxHeight(),
                onClick = {isExpanded = !isExpanded}
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandMore
                                    else Icons.Filled.ExpandLess,
                    contentDescription = "Expand"
                )
            }
            Text(header)
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.toDp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        AnimatedVisibility(
            visible = isExpanded,
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun ShowMeUBits() {
    PickerDrawer(MainViewModel())
}

@Preview
@Composable
fun CollapsibleContentTest() {
    Surface {
        CollapsibleContent("test") {
            Column {
                List(100) {
                    Text("number $it")
                }
            }
        }
    }
}
