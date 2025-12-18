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
import com.example.pepeselforderingapp.viewmodel.CartViewModel

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    outlet: String = "Outlet Brooklyn Tower",
    table: String = "Table A7B",
    cartViewModel: CartViewModel? = null,
    onBackPressed: () -> Unit = {},
    onProceedToPayment: () -> Unit = {}
) {
    // Get cart items from view model
    val cartItems = cartViewModel?.cartItems ?: emptyList()

    // Calculate subtotal from cart items
    val subtotal = cartViewModel?.getSubtotal() ?: 0

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
                        selectedSubitems = item.getSubitemsDisplayString(),
                        price = item.getPriceDisplayString(),
                        quantity = item.quantity,
                        onDecrement = {
                            if (item.quantity > 1) {
                                cartViewModel?.updateQuantity(index, item.quantity - 1)
                            } else {
                                cartViewModel?.removeItem(index)
                            }
                        },
                        onIncrement = {
                            cartViewModel?.updateQuantity(index, item.quantity + 1)
                        },
                        imageUrl = null
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    PepeSelfOrderingAppTheme {
        CartScreen(
            outlet = "Outlet Brooklyn Tower",
            table = "Table A7B",
            cartViewModel = null,
            onBackPressed = {},
            onProceedToPayment = {}
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
            cartViewModel = null,
            onBackPressed = {},
            onProceedToPayment = {}
        )
    }
}
