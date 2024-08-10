package com.tzh.sneakerland.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.tzh.sneakerland.R
import com.tzh.sneakerland.data.local.SharedPreferencesHelper
import com.tzh.sneakerland.navigation.AppNavHost
import com.tzh.sneakerland.navigation.HomeRoute
import com.tzh.sneakerland.navigation.OnBoardRoute
import com.tzh.sneakerland.screen.onBoard.OnBoardScreen
import com.tzh.sneakerland.ui.theme.SnakerLandTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalSharedTransitionApi::class)
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnakerLandTheme {
                var isFirst by remember { mutableStateOf(sharedPreferencesHelper.isFirstTime) }


                AnimatedContent(isFirst) {
                    if (it) {
                        OnBoardScreen {
                            sharedPreferencesHelper.setFirstTime()
                            isFirst = false
                        }
                    } else {

                        var selectedTab by remember { mutableStateOf(BottomBarItem.HOME) }
                        var isDetailScreen by remember { mutableStateOf(false) }
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {
                                AnimatedVisibility(!isDetailScreen) {
                                    MyBottomNavigation(
                                        selectedTab,
                                        onTabSelected = {
                                            selectedTab = it
                                        }
                                    )
                                }
                            },
                        ) { innerPadding ->
                            SharedTransitionLayout(modifier = Modifier.padding(innerPadding)) {
                                AppNavHost(
                                    this,
                                    onDetail = {
                                        isDetailScreen = it
                                    },
                                )
                            }
                        }
                    }
                }


            }
        }
    }

    @Composable
    fun MyBottomNavigation(
        selectedTab: BottomBarItem,
        onTabSelected: (BottomBarItem) -> Unit
    ) {

        val items = BottomBarItem.entries
        var selectedIndex = items.indexOf(selectedTab)
        AnimatedNavigationBar(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(),
            selectedIndex = selectedIndex,
            barColor = MaterialTheme.colorScheme.background,
            ballColor = MaterialTheme.colorScheme.primary,
            cornerRadius = shapeCornerRadius(34.dp),
            ballAnimation = Parabolic(tween(300)),
            indentAnimation = Height(animationSpec = tween(300))
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .noRippleClickable { onTabSelected(item) },
                    contentAlignment = Alignment.Center
                ) {
                    if (item.iconType == IconType.RESOURCE) {
                        Icon(
                            imageVector = item.icon as ImageVector,
                            contentDescription = item.displayName,
                            modifier = Modifier.size(28.dp),
                            tint = if (selectedTab == item) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground

                        )
                    } else {
                        Icon(
                            painter = painterResource(item.icon as Int),
                            contentDescription = item.displayName,
                            modifier = Modifier.size(28.dp),
                            tint = if (selectedTab == item) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun BackgroundColorContent(selected: Boolean) {
        val scale = animateFloatAsState(
            targetValue = if (selected) 1.2f else 1f,
            animationSpec = tween(durationMillis = 500)
        )

        val dropHeight = animateDpAsState(
            targetValue = if (selected) 30.dp else 0.dp,
            animationSpec = tween(durationMillis = 500, easing = EaseOutBounce)
        )

        val color by animateColorAsState(
            if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.White
            }
        )
        Canvas(
            modifier = Modifier
                .size(50.dp)
                .scale(scale.value)
        ) {
            drawRoundRect(
                color = color,
                size = size.copy(height = dropHeight.value.toPx()),
                cornerRadius = CornerRadius(x = size.width, y = size.width)
            )
        }
    }
}


enum class BottomBarItem(
    val displayName: String,
    val icon: Any,
    val iconType: IconType
) {
    HOME(
        displayName = "Home",
        icon = Icons.Outlined.Home,
        iconType = IconType.RESOURCE
    ),
    SHOPPING_CART(
        displayName = "Shopping Cart",
        icon = R.drawable.shopping_bag,
        iconType = IconType.IMAGE
    ),
    SEARCH(
        displayName = "Search",
        icon = Icons.Outlined.Search,
        iconType = IconType.RESOURCE
    ),
    WISH_LIST(
        displayName = "Wish List",
        icon = Icons.Outlined.FavoriteBorder,
        iconType = IconType.RESOURCE
    ),
}


enum class IconType {
    RESOURCE, IMAGE
}

