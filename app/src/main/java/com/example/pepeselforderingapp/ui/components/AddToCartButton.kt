package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.R
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.GreenButton
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun AddToCartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .width(101.dp)
            .height(28.dp)
    ) {
        // Shadow layer - positioned slightly below and behind
        if (enabled)
            Box(
                modifier = Modifier
                    .width(101.dp)
                    .height(28.dp)
                    .offset(y = 4.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(9.dp)
                    )
            )

        // Main button
        Box(
            modifier = Modifier
                .width(101.dp)
                .height(28.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(9.dp),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(
                    color = if (enabled) GreenButton else GreenButton.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(9.dp)
                )
                .clickable(enabled = enabled) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Add To Cart",
                    fontFamily = CarterOne,
                    fontSize = 12.sp,
                    color = BrownDark
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = "Add to cart",
                    tint = BrownDark,
                    modifier = Modifier.size(width = 8.dp, height = 13.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddToCartButtonPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Normal state
            AddToCartButton(
                onClick = {}
            )

            // Disabled state
            AddToCartButton(
                onClick = {},
                enabled = false
            )
        }
    }
}

