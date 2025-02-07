package com.tzh.sneakerland.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakerland.screen.detail.DetailScreen
import com.tzh.sneakerland.screen.home.HomeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
            .navigationBarsPadding(),
        enterTransition = { fadeIn(tween(600)) },
        exitTransition = { fadeOut(tween(600)) },
        sizeTransform = { SizeTransform { _, _ -> spring() } }
    ) {

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
            }
        }
    }
}