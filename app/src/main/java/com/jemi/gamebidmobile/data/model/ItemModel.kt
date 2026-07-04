package com.jemi.gamebidmobile.data.model

data class ItemModel(
    val id: Int,
    val title: String,
    val description: String?,
    val image: String?,
    val starting_price: Double
)