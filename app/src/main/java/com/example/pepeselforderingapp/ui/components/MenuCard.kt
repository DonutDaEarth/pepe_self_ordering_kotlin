package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.theme.Actor
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.GreenMuted
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun MenuCard(
    name: String,
    description: String,
    price: String,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(horizontal = 17.dp)
            .clickable { onAddToCart() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Menu image placeholder
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    color = GreenMuted,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            // TODO: Add actual image loading when imageUrl is provided
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Text content and button
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            // Menu name
            Text(
                text = name,
                fontFamily = CarterOne,
                fontSize = 17.sp,
                color = BrownDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(25.dp)
            )

            // Menu description
            Text(
                text = description,
                fontFamily = Actor,
                fontSize = 11.sp,
                color = BrownDark,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp,
                modifier = Modifier
                    .heightIn(min = 29.dp)
                    .padding(bottom = 9.dp)
            )

            // Price and Add to Cart button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = price,
                    fontFamily = CarterOne,
                    fontSize = 15.sp,
                    color = OrangePrimary
                )

                AddToCartButton(
                    onClick = onAddToCart
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuCardPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Example menu item 1
            MenuCard(
                name = "Pepe Burger",
                description = "Juicy beef patty with fresh lettuce, tomatoes, and our special sauce",
                price = "Rp. 670.000",
                onAddToCart = {}
            )

            // Example menu item 2
            MenuCard(
                name = "Caesar Salad",
                description = "Crispy romaine with parmesan and croutons",
                price = "Rp. 670.000",
                onAddToCart = {}
            )

            // Example with long name and description
            MenuCard(
                name = "Super Deluxe Extra Large Burger",
                description = "This is a very long description that should wrap to multiple lines and show how the card handles overflow text properly",
                price = "Rp. 670.000",
                onAddToCart = {}
            )
        }
    }
}
