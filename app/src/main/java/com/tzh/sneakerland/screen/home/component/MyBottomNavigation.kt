package com.tzh.sneakerland.screen.home.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.tzh.sneakerland.R

@Composable
fun MyBottomNavigation(
    selectedTab: BottomBarItem,
    items: List<BottomBarItem> = BottomBarItem.entries,
    onTabSelected: (BottomBarItem) -> Unit
) {

    val selectedIndex = items.indexOf(selectedTab)

    AnimatedNavigationBar(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth(),
        selectedIndex = selectedIndex,
        barColor = MaterialTheme.colorScheme.background,
        ballColor = MaterialTheme.colorScheme.primary,
        cornerRadius = shapeCornerRadius(38.dp),
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
