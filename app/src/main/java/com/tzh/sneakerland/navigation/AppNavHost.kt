package com.tzh.sneakerland.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakerland.screen.detail.DetailScreen
import com.tzh.sneakerland.screen.home.HomeScreen
import com.tzh.sneakerland.screen.onBoard.OnBoardScreen
import com.tzh.sneakerland.util.Extension.toSneakerModel
import com.tzh.sneakerland.util.Extension.toString
import com.tzh.sneakerland.util.Gender

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    sharedTransitionScope: SharedTransitionScope,
    onDetail: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<OnBoardRoute> {

        }

        composable<HomeRoute> {
            HomeScreen(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this
            ) {
                navController.navigate(
                    DetailRoute(
                        id = it.id,
                        image = it.image,
                        name = it.name
                    )
                )
                onDetail(true)
            }
        }

        composable<DetailRoute> {
            val sneaker = it.toRoute<DetailRoute>()
            DetailScreen(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this,
                SneakerModel(
                    id = sneaker.id,
                    image = sneaker.image,
                    name = sneaker.name,
                )
            ) {
                navController.popBackStack()
                onDetail(false)
            }
        }
    }
}