package com.tzh.sneakerland.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tzh.sneakerland.data.local.SharedPreferencesHelper
import com.tzh.sneakerland.navigation.AppNavHost
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
                AnimatedContent(isFirst, label = "") {
                    if (it) {
                        OnBoardScreen {
                            sharedPreferencesHelper.setFirstTime()
                            isFirst = false
                        }
                    } else {
                        SharedTransitionLayout(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                                AppNavHost(
                                this,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }
            }
        }
    }
}


