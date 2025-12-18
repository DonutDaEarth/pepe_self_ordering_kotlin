package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .height(44.dp)
    ) {
        // Shadow layer - positioned slightly below and behind
        if(enabled)
            Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .offset(y = 4.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        // Main button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(
                    color = if (enabled) OrangePrimary else OrangePrimary.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(enabled = enabled) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontFamily = CarterOne,
                fontSize = 18.sp,
                color = Color(0xFFFEF4E0)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Normal state
            PrimaryButton(
                text = "Login",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            // Another example
            PrimaryButton(
                text = "Sign Up",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            // Disabled state
            PrimaryButton(
                text = "Disabled",
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
