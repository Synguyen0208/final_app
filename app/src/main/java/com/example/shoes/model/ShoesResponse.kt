package com.example.shoes.model

data class ShoesResponse(
    val name: String="",
    val id: Long=0,
    val description: String="",
    val short_description: String="",
    val price:String="",
    val sale_price:String="",
    val categories: Array<Category>?,
    val images: Array<Image>?,
    val price_html:String,
    val attributes: Array<Attribute>?,
    val variations: Array<Int>?
)

data class Image(
    val src:String=""
)
data class Category(
    val id:Int=0,
    val name:String="",
)