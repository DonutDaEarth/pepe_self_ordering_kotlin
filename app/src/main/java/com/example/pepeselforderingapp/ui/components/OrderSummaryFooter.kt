package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.theme.*

enum class OrderSummaryMode {
    CART,
    RECEIPT
}

@Composable
fun OrderSummaryFooter(
    subtotal: Int,
    mode: OrderSummaryMode,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Calculate service charge (10% of subtotal)
    val serviceCharge = (subtotal * 0.10).toInt()

    // Calculate tax (10% of subtotal + service charge)
    val tax = ((subtotal + serviceCharge) * 0.10).toInt()

    // Calculate grand total
    val grandTotal = subtotal + serviceCharge + tax

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(239.dp)
            .background(
                color = BeigeLight,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            )
            .padding(horizontal = 19.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // Subtotal Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(29.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Subtotal",
                fontFamily = CarterOne,
                fontSize = 16.sp,
                color = BrownDark
            )

            Text(
                text = "Rp. ${formatPrice(subtotal)}",
                fontFamily = CarterOne,
                fontSize = 16.sp,
                color = OrangePrimary
            )
        }

        // Service Charge Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(29.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Service Charge (10%)",
                fontFamily = CarterOne,
                fontSize = 16.sp,
                color = BrownDark
            )

            Text(
                text = "+ Rp. ${formatPrice(serviceCharge)}",
                fontFamily = CarterOne,
                fontSize = 16.sp,
                color = OrangePrimary
            )
        }

        // Tax Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(29.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tax (10%)",
                fontFamily = CarterOne,
                fontSize = 16.sp,
                color = BrownDark
            )

            Text(
                text = "+ Rp. ${formatPrice(tax)}",
                fontFamily = CarterOne,
                fontSize = 16.sp,
                color = OrangePrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = OrangeDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Grand Total Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(29.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Grand Total",
                fontFamily = CarterOne,
                fontSize = 18.sp,
                color = BrownDark
            )

            Text(
                text = "Rp. ${formatPrice(grandTotal)}",
                fontFamily = CarterOne,
                fontSize = 18.sp,
                color = OrangePrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button
        PrimaryButton(
            text = when (mode) {
                OrderSummaryMode.CART -> "Proceed to Payment"
                OrderSummaryMode.RECEIPT -> "Make Another Order"
            },
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
        )
    }
}

// Helper function to format price
private fun formatPrice(price: Int): String {
    return price.toString().reversed().chunked(3).joinToString(".").reversed()
}

@Preview(showBackground = true, name = "Cart Mode")
@Composable
fun OrderSummaryFooterCartPreview() {
    PepeSelfOrderingAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CreamBackground)
        ) {
            OrderSummaryFooter(
                subtotal = 125000,
                mode = OrderSummaryMode.CART,
                onButtonClick = { /* Handle proceed to payment */ }
            )
        }
    }
}

@Preview(showBackground = true, name = "Receipt Mode")
@Composable
fun OrderSummaryFooterReceiptPreview() {
    PepeSelfOrderingAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CreamBackground)
        ) {
            OrderSummaryFooter(
                subtotal = 125000,
                mode = OrderSummaryMode.RECEIPT,
                onButtonClick = { /* Handle make another order */ }
            )
        }
    }
}
