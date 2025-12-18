package com.example.pepeselforderingapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.R
import com.example.pepeselforderingapp.ui.theme.*

data class MenuDetail(
    val name: String,
    val description: String,
    val basePrice: Int,
    val imageRes: Int? = null,
    val subitemCategories: List<SubitemCategoryData> = emptyList()
)

data class SubitemCategoryData(
    val title: String,
    val options: List<SubitemOption>
)

@Composable
fun MenuDetailPopup(
    menuDetail: MenuDetail,
    onDismiss: () -> Unit,
    onAddToCart: (selectedSubitems: Map<String, SubitemOption>, quantity: Int) -> Unit,
    modifier: Modifier = Modifier,
    initialIsInCart: Boolean = false,
    initialQuantity: Int = 1
) {
    // Track if item is in cart and quantity
    var isInCart by remember { mutableStateOf(initialIsInCart) }
    var quantity by remember { mutableStateOf(initialQuantity) }

    // Track selected options for each category
    val selectedOptions = remember {
        mutableStateMapOf<String, SubitemOption>()
        // Start with NO selections - user must select from each category
    }

    // Check if all categories have a selection
    val allCategoriesSelected = remember {
        derivedStateOf {
            menuDetail.subitemCategories.all { category ->
                selectedOptions.containsKey(category.title)
            }
        }
    }

    // Calculate total price
    val totalPrice = remember(selectedOptions.size) {
        derivedStateOf {
            val basePrice = menuDetail.basePrice
            val additionalPrice = selectedOptions.values.sumOf { option ->
                // Extract numeric value from price string (e.g., "Rp. 15.000" -> 15000)
                option.price.replace("Rp. ", "")
                    .replace(".", "")
                    .replace(",", "")
                    .replace(" ", "")
                    .replace("-", "-")
                    .toIntOrNull() ?: 0
            }
            basePrice + additionalPrice
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BeigeLight)
    ) {
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Image Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.6f)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                        )
                        .background(
                            color = GreenMuted,
                            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                        )
                ) {
                    // Placeholder for image - will show actual image when provided
                    if (menuDetail.imageRes != null) {
                        Image(
                            painter = painterResource(id = menuDetail.imageRes),
                            contentDescription = menuDetail.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Title
                    Text(
                        text = menuDetail.name,
                        fontFamily = CarterOne,
                        fontSize = 20.sp,
                        color = BrownDark,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description
                    Text(
                        text = menuDetail.description,
                        fontFamily = Actor,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        color = BrownDark,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Subitem Categories
                    menuDetail.subitemCategories.forEach { categoryData ->
                        SubitemCategory(
                            title = categoryData.title,
                            options = categoryData.options,
                            selectedOption = selectedOptions[categoryData.title],
                            onOptionSelected = { option ->
                                selectedOptions[categoryData.title] = option
                            },
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                }

                // Footer with fixed height
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(CreamBackground)
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(9.dp))

                    // Total Price Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Item Price",
                            fontFamily = CarterOne,
                            fontSize = 15.sp,
                            color = OrangePrimary
                        )

                        Text(
                            text = "Rp. ${formatPrice(totalPrice.value)}",
                            fontFamily = CarterOne,
                            fontSize = 15.sp,
                            color = OrangePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(17.dp))

                    // Mode switch: Add To Cart button OR Already in Cart counter
                    if (!isInCart) {
                        // Add To Cart Button Mode
                        PrimaryButton(
                            text = "Add To Cart",
                            onClick = {
                                if (allCategoriesSelected.value) {
                                    isInCart = true
                                    quantity = 1
                                    onAddToCart(selectedOptions.toMap(), quantity)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = allCategoriesSelected.value
                        )
                    } else {
                        // Already in Cart Mode with Counter
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Already In Cart:",
                                fontFamily = CarterOne,
                                fontSize = 25.sp,
                                color = androidx.compose.ui.graphics.Color(0xFF524A42).copy(alpha = 0.75f)
                            )

                            // Counter Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(0.dp)
                            ) {
                                // Decrease Button
                                Button(
                                    onClick = {
                                        if (quantity > 1) {
                                            quantity--
                                            onAddToCart(selectedOptions.toMap(), quantity)
                                        } else {
                                            // When quantity reaches 0, switch back to Add to Cart button
                                            quantity = 0
                                            isInCart = false
                                            onAddToCart(selectedOptions.toMap(), 0)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(36.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = OrangePrimary
                                    ),
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedCornerShape(9.dp)
                                ) {
                                    Text(
                                        text = "-",
                                        fontFamily = CarterOne,
                                        fontSize = 30.sp,
                                        color = androidx.compose.ui.graphics.Color(0xFF524A42)
                                    )
                                }

                                // Quantity Display
                                Text(
                                    text = quantity.toString(),
                                    fontFamily = CarterOne,
                                    fontSize = 30.sp,
                                    color = androidx.compose.ui.graphics.Color(0xFF524A42),
                                    modifier = Modifier.width(48.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )

                                // Increase Button
                                Button(
                                    onClick = {
                                        if (quantity < 99) {
                                            quantity++
                                            onAddToCart(selectedOptions.toMap(), quantity)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(36.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = OrangePrimary
                                    ),
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedCornerShape(9.dp)
                                ) {
                                    Text(
                                        text = "+",
                                        fontFamily = CarterOne,
                                        fontSize = 30.sp,
                                        color = androidx.compose.ui.graphics.Color(0xFF524A42)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(23.dp))
                }
            }
        }

        // Close Button - outside AnimatedVisibility for proper positioning
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-25).dp, y = 67.dp)
                .size(32.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close),
                contentDescription = "Close",
                tint = BrownDark,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Helper function to format price
private fun formatPrice(price: Int): String {
    return price.toString().reversed().chunked(3).joinToString(".").reversed()
}

@Preview(showBackground = true, name = "Add To Cart Mode")
@Composable
fun MenuDetailPopupPreview() {
    PepeSelfOrderingAppTheme {
        var showPopup by remember { mutableStateOf(true) }

        if (showPopup) {
            MenuDetailPopup(
                menuDetail = MenuDetail(
                    name = "Caramel Macchiato",
                    description = "A delicious blend of espresso, steamed milk, and caramel sauce, topped with foam and drizzled with caramel.",
                    basePrice = 45000,
                    subitemCategories = listOf(
                        SubitemCategoryData(
                            title = "Milk Choices",
                            options = listOf(
                                SubitemOption("Regular Milk", "Rp. 0"),
                                SubitemOption("Almond Milk", "Rp. 15.000"),
                                SubitemOption("Oat Milk", "Rp. 12.000"),
                                SubitemOption("Soy Milk", "Rp. 10.000")
                            )
                        ),
                        SubitemCategoryData(
                            title = "Size",
                            options = listOf(
                                SubitemOption("Small", "Rp. -5.000"),
                                SubitemOption("Medium", "Rp. 0"),
                                SubitemOption("Large", "Rp. 8.000"),
                                SubitemOption("Extra Large", "Rp. 15.000")
                            )
                        ),
                        SubitemCategoryData(
                            title = "Sugar Level",
                            options = listOf(
                                SubitemOption("No Sugar", "Rp. 0"),
                                SubitemOption("25%", "Rp. 0"),
                                SubitemOption("50%", "Rp. 0"),
                                SubitemOption("75%", "Rp. 0"),
                                SubitemOption("100%", "Rp. 0")
                            )
                        ),
                        SubitemCategoryData(
                            title = "Ice Level",
                            options = listOf(
                                SubitemOption("No Ice", "Rp. 0"),
                                SubitemOption("Less Ice", "Rp. 0"),
                                SubitemOption("Normal Ice", "Rp. 0"),
                                SubitemOption("Extra Ice", "Rp. 0")
                            )
                        )
                    )
                ),
                onDismiss = { showPopup = false },
                onAddToCart = { _, _ -> /* Handle add to cart */ },
                initialIsInCart = false
            )
        }
    }
}

@Preview(showBackground = true, name = "Already in Cart Mode")
@Composable
fun MenuDetailPopupInCartPreview() {
    PepeSelfOrderingAppTheme {
        var showPopup by remember { mutableStateOf(true) }

        if (showPopup) {
            MenuDetailPopup(
                menuDetail = MenuDetail(
                    name = "Caramel Macchiato",
                    description = "A delicious blend of espresso, steamed milk, and caramel sauce, topped with foam and drizzled with caramel.",
                    basePrice = 45000,
                    subitemCategories = listOf(
                        SubitemCategoryData(
                            title = "Milk Choices",
                            options = listOf(
                                SubitemOption("Regular Milk", "Rp. 0"),
                                SubitemOption("Almond Milk", "Rp. 15.000"),
                                SubitemOption("Oat Milk", "Rp. 12.000"),
                                SubitemOption("Soy Milk", "Rp. 10.000")
                            )
                        ),
                        SubitemCategoryData(
                            title = "Size",
                            options = listOf(
                                SubitemOption("Small", "Rp. -5.000"),
                                SubitemOption("Medium", "Rp. 0"),
                                SubitemOption("Large", "Rp. 8.000"),
                                SubitemOption("Extra Large", "Rp. 15.000")
                            )
                        ),
                        SubitemCategoryData(
                            title = "Sugar Level",
                            options = listOf(
                                SubitemOption("No Sugar", "Rp. 0"),
                                SubitemOption("25%", "Rp. 0"),
                                SubitemOption("50%", "Rp. 0"),
                                SubitemOption("75%", "Rp. 0"),
                                SubitemOption("100%", "Rp. 0")
                            )
                        ),
                        SubitemCategoryData(
                            title = "Ice Level",
                            options = listOf(
                                SubitemOption("No Ice", "Rp. 0"),
                                SubitemOption("Less Ice", "Rp. 0"),
                                SubitemOption("Normal Ice", "Rp. 0"),
                                SubitemOption("Extra Ice", "Rp. 0")
                            )
                        )
                    )
                ),
                onDismiss = { showPopup = false },
                onAddToCart = { _, _ -> /* Handle add to cart */ },
                initialIsInCart = true,
                initialQuantity = 3
            )
        }
    }
}
