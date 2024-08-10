package com.tzh.sneakerland.navigation

import com.tzh.sneakerland.data.model.SneakerModel
import kotlinx.serialization.Serializable


@Serializable
data object OnBoardRoute

@Serializable
data object HomeRoute


@Serializable
data class DetailRoute(
    val id: Int,
)
