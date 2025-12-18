package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Text(
        text = text,
        fontFamily = CarterOne,
        fontSize = 14.sp,
        color = if (enabled) OrangePrimary else OrangePrimary.copy(alpha = 0.5f),
        modifier = modifier.clickable(enabled = enabled) { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun TextButtonPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Normal state
            TextButton(
                text = "Forgot Password?",
                onClick = {}
            )

            // Another example
            TextButton(
                text = "Don't have an account? Sign Up",
                onClick = {}
            )

            // Disabled state
            TextButton(
                text = "Disabled Text Button",
                onClick = {},
                enabled = false
            )
        }
    }
}

