package com.tzh.sneakerland.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tzh.sneakerland.data.model.dummySneakerList
import com.tzh.sneakerland.screen.detail.DetailScreen
import com.tzh.sneakerland.screen.home.HomeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(sharedTransitionScope: SharedTransitionScope) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this
            ) {
                navController.navigate(
                    DetailRoute(
                        it.id
                    )
                )
            }
        }

        composable<DetailRoute> {
            val args = it.toRoute<DetailRoute>()
            DetailScreen(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this,
                dummySneakerList.find { it.id == args.id }!!
            ) {

            }
        }
    }
}