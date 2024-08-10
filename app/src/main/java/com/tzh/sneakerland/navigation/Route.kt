package com.tzh.sneakerland.navigation

import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakerland.util.Gender
import kotlinx.serialization.Serializable


@Serializable
data object OnBoardRoute

@Serializable
data object HomeRoute


@Serializable
data class DetailRoute(
    val id: Int,
    val image: String,
    val name: String,

    )
