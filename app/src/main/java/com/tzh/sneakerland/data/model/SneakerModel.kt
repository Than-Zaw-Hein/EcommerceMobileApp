package com.tzh.sneakerland.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tzh.sneakerland.R
import kotlinx.serialization.Serializable


@Serializable
data class SneakerModel(
    val id: Int,
    val name: String,
    val brand: String,
    val description: String,
    val image: Int,
    val price: String = "$120",
) {
    var isFavourite by mutableStateOf(false)
}

val dummySneakerList = listOf(
    SneakerModel(
        1,
        "New Balance 997",
        brand = "Nike",
        "Stylish and comfortable sneaker featuring a mix of suede and mesh upper for durability and breathability.",
        R.drawable.new_balance_997,
    ),
    SneakerModel(
        2,
        "Vans Old Skool Black",
        brand = "Van",
        "Classic skate shoe with the iconic side stripe. Made with durable canvas and suede uppers.",
        R.drawable.vans_old_school_blue,
    ),
    SneakerModel(
        3,
        "Vans Old Skool Blue",
        brand = "Vans",
        "Two-tone blue and black design with the signature side stripe. Constructed with durable canvas and suede uppers.",
        R.drawable.new_balance_997,
    ),
    SneakerModel(
        4,
        "Adidas X 18.1 Soccer Cleats",
        brand = "Adidas",
        "Designed for speed and agility on the field. Lightweight, thin mesh upper, low-cut Clawcollar, and arrowhead forefoot studs.",
        R.drawable.adidas_x_soccer_cleats,
    ),
    SneakerModel(
        5,
        "Brown Leather High-Top Sneakers",
        brand = "Zappos",
        "Casual style and comfort. Made from premium leather, cushioned insole, lace-up closure, and durable rubber outsole.",
        R.drawable.brown_leather_high_top_sneakers,
    )
)