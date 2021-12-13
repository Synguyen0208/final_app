package com.example.shoes.ui.home.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoes.R
import com.example.shoes.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Holds the contents of the cart and allows changes to it.
 *
 * TODO: Move data to Repository so it can be displayed and changed consistently throughout the app.
 */
class CartViewModel(
    private val shoesbarManager: ShoesbarManager,
    snackRepository: ShoesRepo
) : ViewModel() {

    private val _orderLines: MutableStateFlow<List<OrderLine>> =
        MutableStateFlow(snackRepository.getCart())
    val orderLines: StateFlow<List<OrderLine>> get() = _orderLines

    // Logic to show errors every few requests
    private var requestCount = 0
    private fun shouldRandomlyFail(): Boolean = ++requestCount % 5 == 0

    fun increaseSnackCount(shoesId: Long) {
        if (!shouldRandomlyFail()) {
            val currentCount = _orderLines.value.first { it.shoes.id == shoesId }.count
            updateSnackCount(shoesId, currentCount + 1)
        } else {
            shoesbarManager.showMessage(R.string.cart_increase_error)
        }
    }

    fun decreaseSnackCount(shoesId: Long) {
        if (!shouldRandomlyFail()) {
            val currentCount = _orderLines.value.first { it.shoes.id == shoesId }.count
            if (currentCount == 1) {
                removeShoes(shoesId)
            } else {
                updateSnackCount(shoesId, currentCount - 1)
            }
        } else {
            shoesbarManager.showMessage(R.string.cart_decrease_error)
        }
    }

    fun removeShoes(shoesId: Long) {
        _orderLines.value = _orderLines.value.filter { it.shoes.id != shoesId }
    }
    fun addShoes(shoes: ShoesResponse) {
        _orderLines.value = _orderLines.value.plus(OrderLine(shoes, 2))
    }
    private fun updateSnackCount(shoesId: Long, count: Int) {
        _orderLines.value = _orderLines.value.map {
            if (it.shoes.id == shoesId) {
                it.copy(count = count)
            } else {
                it
            }
        }
    }
    companion object {
        fun provideFactory(
            shoesbarManager: ShoesbarManager = ShoesbarManager,
            snackRepository: ShoesRepo = ShoesRepo
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CartViewModel(shoesbarManager, snackRepository) as T
            }
        }
    }
}
