package com.tzh.sneakerland.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AnimateSneakerImage(
    @DrawableRes imageResId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    with(sharedTransitionScope) {
        SneakerImage(
            imageResId,
            Modifier .sharedElement(
                state = rememberSharedContentState("image/$imageResId"),
                animatedContentScope
            )
        )
    }
}

@Composable
fun SneakerImage(
    @DrawableRes imageResId: Int,
    modifier: Modifier
) {
    Image(
        painter = painterResource(imageResId),
        contentDescription = imageResId.toString(),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
    )
}