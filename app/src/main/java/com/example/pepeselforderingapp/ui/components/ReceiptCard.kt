package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pepeselforderingapp.ui.theme.Actor
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.GreenMuted
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun ReceiptCard(
    name: String,
    selectedSubitems: String,
    totalPrice: String,
    quantity: Int,
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 31.dp)
    ) {
        // Menu image with loading state
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = GreenMuted,
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = OrangePrimary,
                                strokeWidth = 3.dp
                            )
                        }
                    },
                    error = {
                        // Show placeholder on error (green background already visible)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            // Menu name with quantity
            val displayName = if (quantity > 1) "${quantity}x $name" else name
            Text(
                text = displayName,
                fontFamily = CarterOne,
                fontSize = 17.sp,
                color = BrownDark
            )

            // Selected subitems
            Text(
                text = selectedSubitems,
                fontFamily = Actor,
                fontSize = 11.sp,
                color = BrownDark,
                lineHeight = 14.sp
            )

            // Total price
            Text(
                text = totalPrice,
                fontFamily = CarterOne,
                fontSize = 15.sp,
                color = OrangePrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptCardPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Example receipt item 1 - single quantity
            ReceiptCard(
                name = "Pepe Burger",
                selectedSubitems = "Almond Milk, Extra Cheese, No Onions",
                totalPrice = "Rp. 670.000",
                quantity = 1
            )

            // Example receipt item 2 - multiple quantity
            ReceiptCard(
                name = "Caesar Salad",
                selectedSubitems = "Regular Dressing, Add Chicken",
                totalPrice = "Rp. 1.350.000",
                quantity = 3
            )

            // Example with long name and subitems
            ReceiptCard(
                name = "Super Deluxe Burger",
                selectedSubitems = "Oat Milk, Extra Lettuce, Extra Tomato, Extra Pickles, Add Bacon, Extra Sauce",
                totalPrice = "Rp. 4.450.000",
                quantity = 5
            )

            // Example with very long subitems to test wrapping
            ReceiptCard(
                name = "Custom Pizza",
                selectedSubitems = "Extra Cheese, Pepperoni, Mushrooms, Olives, Bell Peppers, Onions, Sausage, Ham, Bacon, Pineapple, Jalapeños",
                totalPrice = "Rp. 2.250.000",
                quantity = 2
            )
        }
    }
}
