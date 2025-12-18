package com.example.pepeselforderingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.components.*
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.CreamBackground
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

data class ReceiptItem(
    val name: String,
    val selectedSubitems: String,
    val totalPrice: String,
    val quantity: Int,
    val imageUrl: String? = null
)

@Composable
fun ReceiptScreen(
    modifier: Modifier = Modifier,
    table: String = "Table A7B",
    outlet: String = "Outlet Brooklyn Tower",
    receiptItems: List<ReceiptItem> = emptyList(),
    onMakeAnotherOrder: () -> Unit = {}
) {
    // Calculate subtotal from receipt items
    val subtotal = receiptItems.sumOf { item ->
        parsePriceFromString(item.totalPrice)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top section with table and outlet info
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 23.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Table number
                Text(
                    text = table,
                    fontFamily = CarterOne,
                    fontSize = 20.sp,
                    color = BrownDark
                )

                // Outlet name
                Text(
                    text = outlet,
                    fontFamily = CarterOne,
                    fontSize = 17.sp,
                    color = BrownDark
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Scrollable receipt items
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                receiptItems.forEachIndexed { index, item ->
                    ReceiptCard(
                        name = item.name,
                        selectedSubitems = item.selectedSubitems,
                        totalPrice = item.totalPrice,
                        quantity = item.quantity,
                        imageUrl = item.imageUrl
                    )

                    if (index < receiptItems.size - 1) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Add bottom padding for better scrolling
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Order Summary Footer with "Make Another Order" button
            OrderSummaryFooter(
                subtotal = subtotal,
                mode = OrderSummaryMode.RECEIPT,
                onButtonClick = onMakeAnotherOrder
            )
        }
    }
}

// Helper function to parse price string
private fun parsePriceFromString(priceString: String): Int {
    return priceString.replace("Rp. ", "").replace(".", "").toIntOrNull() ?: 0
}

@Preview(showBackground = true)
@Composable
fun ReceiptScreenPreview() {
    PepeSelfOrderingAppTheme {
        ReceiptScreen(
            table = "Table A7B",
            outlet = "Outlet Brooklyn Tower",
            receiptItems = listOf(
                ReceiptItem(
                    name = "Pepe Burger",
                    selectedSubitems = "Almond Milk, Extra Cheese, No Onions",
                    totalPrice = "Rp. 670.000",
                    quantity = 3
                ),
                ReceiptItem(
                    name = "Caesar Salad",
                    selectedSubitems = "Regular Dressing, Add Chicken",
                    totalPrice = "Rp. 450.000",
                    quantity = 2
                ),
                ReceiptItem(
                    name = "Cappuccino",
                    selectedSubitems = "Oat Milk, Extra Shot",
                    totalPrice = "Rp. 125.000",
                    quantity = 1
                ),
                ReceiptItem(
                    name = "French Fries",
                    selectedSubitems = "Large Size, Extra Salt",
                    totalPrice = "Rp. 180.000",
                    quantity = 2
                ),
                ReceiptItem(
                    name = "Chocolate Cake",
                    selectedSubitems = "With Ice Cream",
                    totalPrice = "Rp. 225.000",
                    quantity = 1
                )
            ),
            onMakeAnotherOrder = {}
        )
    }
}

