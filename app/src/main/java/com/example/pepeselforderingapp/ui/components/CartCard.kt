package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pepeselforderingapp.ui.theme.Actor
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.GreenButton
import com.example.pepeselforderingapp.ui.theme.GreenMuted
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun CartCard(
    name: String,
    selectedSubitems: String,
    price: String,
    quantity: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(horizontal = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Menu image with loading state
        Box(
            modifier = Modifier
                .size(96.dp)
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
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
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

        Spacer(modifier = Modifier.width(14.dp))

        // Text content and quantity controls
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

            // Selected subitems (replaces description)
            Text(
                text = selectedSubitems,
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

            // Price and quantity controls
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

                // Quantity counter row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // Decrement button
                    Box(
                        modifier = Modifier
                            .size(27.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(8.dp),
                                spotColor = Color.Black.copy(alpha = 0.25f)
                            )
                            .background(
                                color = GreenButton,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onDecrement() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "-",
                            fontFamily = CarterOne,
                            fontSize = 20.sp,
                            color = BrownDark
                        )
                    }

                    // Quantity display
                    Text(
                        text = quantity.toString(),
                        fontFamily = CarterOne,
                        fontSize = 15.sp,
                        color = BrownDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(47.dp)
                    )

                    // Increment button
                    Box(
                        modifier = Modifier
                            .size(27.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(8.dp),
                                spotColor = Color.Black.copy(alpha = 0.25f)
                            )
                            .background(
                                color = GreenButton,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onIncrement() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            fontFamily = CarterOne,
                            fontSize = 20.sp,
                            color = BrownDark
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartCardPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Example cart item 1
            CartCard(
                name = "Pepe Burger",
                selectedSubitems = "Almond Milk, Extra Cheese, No Onions",
                price = "Rp. 670.000",
                quantity = 2,
                onDecrement = {},
                onIncrement = {}
            )

            // Example cart item 2
            CartCard(
                name = "Caesar Salad",
                selectedSubitems = "Regular Dressing, Add Chicken",
                price = "Rp. 450.000",
                quantity = 1,
                onDecrement = {},
                onIncrement = {}
            )

            // Example with long name and subitems
            CartCard(
                name = "Super Deluxe Burger",
                selectedSubitems = "Oat Milk, Extra Lettuce, Extra Tomato, Extra Pickles, Add Bacon",
                price = "Rp. 890.000",
                quantity = 5,
                onDecrement = {},
                onIncrement = {}
            )
        }
    }
}
