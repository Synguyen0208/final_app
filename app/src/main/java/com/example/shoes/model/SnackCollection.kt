
package com.example.shoes.model

import androidx.compose.runtime.Immutable

@Immutable
data class SnackCollection(
    val id: Long,
    val name: String,
    val snacks: List<Snack>,
    val type: CollectionType = CollectionType.Normal
)

enum class CollectionType { Normal, Highlight }

/**
 * A fake repo
 */
object ShoesRepo {
    fun getCart() = cart
}

val image= arrayOf(
    Image("fgdgf")
)
val item=ShoesResponse("fgffg",66, "gfrtgtrth", "jhhjhhjh", "6576", "6576578", arrayOf(Category(6, "Jordan")), image, "4546", arrayOf(Attribute(5, "size", listOf("hhh"))), arrayOf(7))
private var cart = listOf(
    OrderLine(item, 2),
)

@Immutable
data class OrderLine(
    val shoes: ShoesResponse,
    val count: Int
)
