package com.tzh.sneakerland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakerland.navigation.AppNavHost
import com.tzh.sneakerland.navigation.DetailRoute
import com.tzh.sneakerland.navigation.HomeRoute
import com.tzh.sneakerland.screen.detail.DetailScreen
import com.tzh.sneakerland.screen.home.HomeScreen
import com.tzh.sneakerland.ui.theme.SnakerLandTheme

@OptIn(ExperimentalSharedTransitionApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnakerLandTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {

                    },
                ) { innerPadding ->
                    SharedTransitionLayout(modifier = Modifier.padding(innerPadding)) {
                        AppNavHost(this)
                    }
                }
            }
        }
    }

}

