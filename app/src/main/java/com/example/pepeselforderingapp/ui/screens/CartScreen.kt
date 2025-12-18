package com.example.pepeselforderingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pepeselforderingapp.ui.components.*
import com.example.pepeselforderingapp.ui.theme.CreamBackground
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

data class CartItem(
    val name: String,
    val selectedSubitems: String,
    val price: String,
    val quantity: Int,
    val imageUrl: String? = null
)

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    outlet: String = "Outlet Brooklyn Tower",
    table: String = "Table A7B",
    cartItems: List<CartItem> = emptyList(),
    onBackPressed: () -> Unit = {},
    onProceedToPayment: () -> Unit = {},
    onQuantityChange: (Int, Int) -> Unit = { _, _ -> }
) {
    // Calculate subtotal from cart items
    val subtotal = cartItems.sumOf { item ->
        parsePriceFromString(item.price) * item.quantity
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header in CART mode
            Header(
                mode = HeaderMode.CART,
                onBackClick = onBackPressed,
                outletName = outlet,
                tableNumber = table
            )

            // Scrollable cart items
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(28.dp))

                cartItems.forEachIndexed { index, item ->
                    CartCard(
                        name = item.name,
                        selectedSubitems = item.selectedSubitems,
                        price = item.price,
                        quantity = item.quantity,
                        onDecrement = {
                            if (item.quantity > 1) {
                                onQuantityChange(index, item.quantity - 1)
                            }
                        } ,
                        onIncrement = {
                            onQuantityChange(index, item.quantity + 1)
                        },
                        imageUrl = item.imageUrl
                    )

                    if (index < cartItems.size - 1) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Add bottom padding for better scrolling
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Order Summary Footer
            OrderSummaryFooter(
                subtotal = subtotal,
                mode = OrderSummaryMode.CART,
                onButtonClick = onProceedToPayment
            )
        }
    }
}

// Helper function to parse price string
private fun parsePriceFromString(priceString: String): Int {
    return priceString.replace("Rp. ", "").replace(".", "").toIntOrNull() ?: 0
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    PepeSelfOrderingAppTheme {
        CartScreen(
            outlet = "Outlet Brooklyn Tower",
            table = "Table A7B",
            cartItems = listOf(
                CartItem(
                    name = "Pepe Burger",
                    selectedSubitems = "Almond Milk, Large Size",
                    price = "Rp. 60.000",
                    quantity = 2
                ),
                CartItem(
                    name = "Iced Coffee",
                    selectedSubitems = "Regular Milk, Regular Size",
                    price = "Rp. 25.000",
                    quantity = 1
                ),
                CartItem(
                    name = "French Fries",
                    selectedSubitems = "Extra Salt, Large",
                    price = "Rp. 20.000",
                    quantity = 3
                ),
                CartItem(
                    name = "Chicken Wings",
                    selectedSubitems = "Spicy, Ranch Dip",
                    price = "Rp. 35.000",
                    quantity = 1
                ),
                CartItem(
                    name = "Cheese Burger",
                    selectedSubitems = "Regular Milk, Extra Cheese",
                    price = "Rp. 55.000",
                    quantity = 2
                )
            ),
            onBackPressed = {},
            onProceedToPayment = {},
            onQuantityChange = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Empty Cart")
@Composable
fun CartScreenEmptyPreview() {
    PepeSelfOrderingAppTheme {
        CartScreen(
            outlet = "Outlet Brooklyn Tower",
            table = "Table A7B",
            cartItems = emptyList(),
            onBackPressed = {},
            onProceedToPayment = {},
            onQuantityChange = { _, _ -> }
        )
    }
}

