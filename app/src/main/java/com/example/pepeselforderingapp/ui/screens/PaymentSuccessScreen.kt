package com.example.pepeselforderingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.components.SpinningLogo
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.CreamBackground
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun PaymentSuccessScreen(
    modifier: Modifier = Modifier,
    onNavigateToReceipt: () -> Unit = {}
) {
    // Automatically navigate to receipt after 3 seconds
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        onNavigateToReceipt()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Spinning Logo
            SpinningLogo()

            Spacer(modifier = Modifier.height(40.dp))

            // Payment Successful Text
            Text(
                text = "Payment Successful!",
                fontFamily = CarterOne,
                fontSize = 26.sp,
                color = OrangePrimary
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentSuccessScreenPreview() {
    PepeSelfOrderingAppTheme {
        PaymentSuccessScreen(
            onNavigateToReceipt = {}
        )
    }
}
