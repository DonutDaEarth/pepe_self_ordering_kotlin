package com.example.pepeselforderingapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.pepeselforderingapp.ui.components.SubitemOption

data class CartItemData(
    val name: String,
    val description: String,
    val basePrice: Int,
    val selectedSubitems: Map<String, SubitemOption>,
    val quantity: Int = 1,
    val imageRes: Int? = null,
    val menuId: Int = 0,  // Add menu ID (m_id from API)
    val subitemIds: List<Int> = emptyList()  // Add subitem IDs for order creation
) {
    // Calculate total price for this item including subitems
    fun getTotalPrice(): Int {
        val subitemsPrice = selectedSubitems.values.sumOf { option ->
            option.price.replace("Rp. ", "")
                .replace(".", "")
                .replace(",", "")
                .replace(" ", "")
                .toIntOrNull() ?: 0
        }
        return (basePrice + subitemsPrice) * quantity
    }

    // Format selected subitems as a display string
    fun getSubitemsDisplayString(): String {
        return selectedSubitems.values.joinToString(", ") { it.name }
    }

    // Format price as display string
    fun getPriceDisplayString(): String {
        val price = getTotalPrice()
        return "Rp. ${formatPrice(price)}"
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed()
    }
}

class CartViewModel : ViewModel() {
    private val _cartItems: SnapshotStateList<CartItemData> = mutableStateListOf()
    val cartItems: List<CartItemData> get() = _cartItems

    fun addToCart(item: CartItemData) {
        // Check if the same item with same base price and same subitems already exists
        val existingItemIndex = _cartItems.indexOfFirst { cartItem ->
            cartItem.name == item.name &&
            cartItem.basePrice == item.basePrice &&  // Also check base price
            cartItem.selectedSubitems == item.selectedSubitems
        }

        if (existingItemIndex != -1) {
            // Update quantity if item already exists
            val existingItem = _cartItems[existingItemIndex]
            _cartItems[existingItemIndex] = existingItem.copy(
                quantity = existingItem.quantity + item.quantity
            )
        } else {
            // Add as new item
            _cartItems.add(item)
        }
    }

    fun updateQuantity(index: Int, newQuantity: Int) {
        if (index in _cartItems.indices && newQuantity > 0) {
            _cartItems[index] = _cartItems[index].copy(quantity = newQuantity)
        }
    }

    fun removeItem(index: Int) {
        if (index in _cartItems.indices) {
            _cartItems.removeAt(index)
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun getSubtotal(): Int {
        return _cartItems.sumOf { it.getTotalPrice() }
    }

    fun isCartEmpty(): Boolean {
        return _cartItems.isEmpty()
    }
}
