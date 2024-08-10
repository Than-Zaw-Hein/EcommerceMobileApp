package com.tzh.sneakerland.data.model


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.tzh.sneakerland.util.Gender
import kotlinx.serialization.Serializable


@Serializable
data class SneakerModel(
    val id: Int,
    val name: String,
    val brand: String = "",
    val gender: Gender = Gender.UNISEX,
    val description: String = "",
    val image: String,
    val price: Double = 120.0,
    val rating: Double = 0.0,
    var isFavourite: Boolean = false,
) {
    var currentQty by mutableStateOf(1)
    var selectedSize by mutableStateOf<Double?>(null)
    var selectedColor by mutableStateOf<String?>(null)

    constructor() : this(
        id = 0,
        name = "",
        brand = "",
        gender = Gender.UNISEX,
        description = "",
        image = "",
        price = 0.0,
        rating = 0.0,
        isFavourite = false
    )

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

val dummySneakerList = listOf(
    SneakerModel(
        1,
        "New Balance 997",
        brand = "Nike",
        gender = Gender.MALE,
        "Stylish and comfortable sneaker featuring a mix of suede and mesh upper for durability and breathability.",
        "https://media.istockphoto.com/id/1436061606/photo/flying-colorful-womens-sneaker-isolated-on-white-background-fashionable-stylish-sports-shoe.jpg?s=612x612&w=0&k=20&c=2KKjX9tXo0ibmBaPlflnJNdtZ-J77wrprVStaPL2Gj4=",
        rating = 2.0
    ),
    SneakerModel(
        2,
        "Vans Old Skool Black",
        brand = "Vans",
        gender = Gender.MALE,
        "Classic skate shoe with the iconic side stripe. Made with durable canvas and suede uppers.",
        "https://media.istockphoto.com/id/1337191336/photo/black-fashion-sport-shoe-on-white-background.jpg?s=612x612&w=0&k=20&c=7Z8JNZ-nDPpDMHC7olOXrQm6uT3fG9Ya3h8uh_hpVbw=",
        rating = 3.4
    ),
    SneakerModel(
        3,
        "Vans Old Skool Blue",
        brand = "Vans",
        gender = Gender.MALE,
        "Two-tone blue and black design with the signature side stripe. Constructed with durable canvas and suede uppers.",
        "https://media.istockphoto.com/id/1249496770/photo/running-shoes.jpg?s=612x612&w=0&k=20&c=b4MahNlk4LH6H1ksJApfnlQ5ZPM3KGhI5i_yqhGD9c4=",
        rating = 3.7
    ),
    SneakerModel(
        4,
        "Adidas X 18.1 Soccer Cleats",
        brand = "Adidas",
        gender = Gender.FEMALE,
        "Designed for speed and agility on the field. Lightweight, thin mesh upper, low-cut Clawcollar, and arrowhead forefoot studs.",
        "https://media.istockphoto.com/id/1308274455/photo/blue-sneakers-isolated-on-white-background.jpg?s=612x612&w=0&k=20&c=mNrdHQkWjTk8xxEn9Dst9C-ouTemFo-8dI5vpa1yfjk=",
        rating = 4.2
    ),
    SneakerModel(
        5,
        "Brown Leather High-Top Sneakers",
        brand = "Puma",
        gender = Gender.FEMALE,
        "Casual style and comfort. Made from premium leather, cushioned insole, lace-up closure, and durable rubber outsole.",
        "https://media.istockphoto.com/id/956501428/photo/sport-shoes-on-isolated-white-background.jpg?s=612x612&w=0&k=20&c=BdklqnfGUvf02-2CxYsw-AnrbE3e-B5zhE9JQILEEW4= ",
        rating = 5.0
    ),
    SneakerModel(
        id = 6,
        name = "Red Suede Classic Sneakers",
        brand = "Adidas",
        gender = Gender.UNISEX,
        description = "Classic sneakers in red suede with a low-top design. Features cushioned insole, lace-up closure, and durable rubber outsole.",
        image = "https://media.istockphoto.com/id/1337191336/photo/red-suede-classic-sneakers.jpg?s=612x612&w=0&k=20&c=8zyUmXv9RR4M-6YgmcHO4NlPzOMFVdb8UOBP1tXn2Rc=",
        rating = 4.7
    ),

    SneakerModel(
        id = 7,
        name = "Black Leather Low-Top Sneakers",
        brand = "Reebok",
        gender = Gender.MALE,
        description = "Premium black leather low-top sneakers with a minimalist design. Features soft cushioning and a sleek profile for everyday wear.",
        image = "https://media.istockphoto.com/id/1308274455/photo/black-leather-sneakers.jpg?s=612x612&w=0&k=20&c=FqEV7P72Zy8K4W60USqlsYYZT9X7B_jTkdSMGnsX4uM=",
        rating = 4.3
    ),

    SneakerModel(
        id = 8,
        name = "White Mesh Running Shoes",
        brand = "Nike",
        gender = Gender.FEMALE,
        description = "Lightweight white running shoes with breathable mesh upper. Ideal for daily runs, featuring a cushioned sole and excellent grip.",
        image = "https://media.istockphoto.com/id/1436061606/photo/white-mesh-running-shoes.jpg?s=612x612&w=0&k=20&c=snOe6c8BSNkHStzEZJXb2Y_HKyd7SwbbdCRNqIzCjHg=",
        rating = 4.8
    ),

    SneakerModel(
        id = 9,
        name = "Grey Knit Slip-On Sneakers",
        brand = "New Balance",
        gender = Gender.UNISEX,
        description = "Comfortable grey knit slip-on sneakers. Features an elasticized collar, cushioned footbed, and lightweight design for all-day wear.",
        image = "https://media.istockphoto.com/id/1453810805/photo/grey-knit-slip-on-sneakers.jpg?s=612x612&w=0&k=20&c=fbO8jZwIp4ShSCh_NZJQfgF0c2xJH1FOSmYykgy8ycU=",
        rating = 4.6
    ),

    SneakerModel(
        id = 10,
        name = "Green Canvas High-Top Sneakers",
        brand = "Converse",
        gender = Gender.MALE,
        description = "Classic green canvas high-top sneakers with a timeless design. Durable construction with a rubber toe cap and vulcanized sole.",
        image = "https://media.istockphoto.com/id/956501428/photo/green-canvas-high-top-sneakers.jpg?s=612x612&w=0&k=20&c=QJb4fD42ZbX4TOowvSWEFCmDbH1l6fLC6H8-K9fTwM4=",
        rating = 4.9
    ),
)