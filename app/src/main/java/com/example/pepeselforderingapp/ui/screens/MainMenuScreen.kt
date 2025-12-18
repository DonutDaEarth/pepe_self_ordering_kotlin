package com.example.pepeselforderingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pepeselforderingapp.ui.components.*
import com.example.pepeselforderingapp.ui.theme.CreamBackground
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme
import com.example.pepeselforderingapp.viewmodel.CartItemData
import com.example.pepeselforderingapp.viewmodel.CartViewModel

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    outlet: String = "Outlet Brooklyn Tower",
    table: String = "Table A7B",
    cartViewModel: CartViewModel? = null,
    onBackPressed: () -> Unit = {},
    onSearchQuery: (String) -> Unit = {},
    onProceedToCart: () -> Unit = {}
) {
    var showMenuDetail by remember { mutableStateOf(false) }
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(null) }
    var selectedMenuDetail by remember { mutableStateOf<MenuDetail?>(null) }

    // Check if cart has items
    val hasItemsInCart = cartViewModel?.cartItems?.isNotEmpty() ?: false

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header in MAIN_MENU mode
            Header(
                mode = HeaderMode.MAIN_MENU,
                onBackClick = onBackPressed,
                outletName = outlet,
                tableNumber = table,
                onSearchChange = onSearchQuery
            )

            // Scrollable content with menu categories
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = if (hasItemsInCart) 80.dp else 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(11.dp))

                    // Mock data - First category
                    MenuCategory(
                        categoryName = "Burgers",
                        menuItems = listOf(
                            MenuItem(
                                name = "Pepe Burger",
                                description = "Classic beef burger with lettuce, tomato, and special sauce",
                                price = "Rp. 45.000",
                                imageUrl = null
                            ),
                            MenuItem(
                                name = "Cheese Burger",
                                description = "Juicy beef patty with melted cheese and pickles",
                                price = "Rp. 50.000",
                                imageUrl = null
                            ),
                            MenuItem(
                                name = "Chicken Burger",
                                description = "Crispy chicken fillet with mayo and fresh vegetables",
                                price = "Rp. 42.000",
                                imageUrl = null
                            )
                        ),
                        onAddToCart = { menuItem ->
                            selectedMenuItem = menuItem
                            selectedMenuDetail = MenuDetail(
                                name = menuItem.name,
                                description = menuItem.description,
                                basePrice = parsePriceFromString(menuItem.price),
                                imageRes = null,
                                subitemCategories = listOf(
                                    SubitemCategoryData(
                                        title = "Patty Cook Level",
                                        options = listOf(
                                            SubitemOption("Rare", "Rp. 0"),
                                            SubitemOption("Medium Rare", "Rp. 0"),
                                            SubitemOption("Medium", "Rp. 0"),
                                            SubitemOption("Well Done", "Rp. 0")
                                        )
                                    ),
                                    SubitemCategoryData(
                                        title = "Add-ons",
                                        options = listOf(
                                            SubitemOption("No Add-ons", "Rp. 0"),
                                            SubitemOption("Extra Cheese", "Rp. 5.000"),
                                            SubitemOption("Bacon", "Rp. 8.000"),
                                            SubitemOption("Fried Egg", "Rp. 5.000")
                                        )
                                    )
                                )
                            )
                            showMenuDetail = true
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Mock data - Second category
                    MenuCategory(
                        categoryName = "Drinks",
                        menuItems = listOf(
                            MenuItem(
                                name = "Iced Coffee",
                                description = "Cold brew coffee served with ice",
                                price = "Rp. 25.000",
                                imageUrl = null
                            ),
                            MenuItem(
                                name = "Latte",
                                description = "Espresso with steamed milk and foam",
                                price = "Rp. 30.000",
                                imageUrl = null
                            ),
                            MenuItem(
                                name = "Fresh Orange Juice",
                                description = "Freshly squeezed orange juice",
                                price = "Rp. 20.000",
                                imageUrl = null
                            )
                        ),
                        onAddToCart = { menuItem ->
                            selectedMenuItem = menuItem
                            selectedMenuDetail = MenuDetail(
                                name = menuItem.name,
                                description = menuItem.description,
                                basePrice = parsePriceFromString(menuItem.price),
                                imageRes = null,
                                subitemCategories = listOf(
                                    SubitemCategoryData(
                                        title = "Size",
                                        options = listOf(
                                            SubitemOption("Regular", "Rp. 0"),
                                            SubitemOption("Large", "Rp. 10.000")
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
                            )
                            showMenuDetail = true
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Mock data - Third category
                    MenuCategory(
                        categoryName = "Sides",
                        menuItems = listOf(
                            MenuItem(
                                name = "French Fries",
                                description = "Crispy golden fries with sea salt",
                                price = "Rp. 15.000",
                                imageUrl = null
                            ),
                            MenuItem(
                                name = "Onion Rings",
                                description = "Battered and fried onion rings",
                                price = "Rp. 18.000",
                                imageUrl = null
                            ),
                            MenuItem(
                                name = "Chicken Wings",
                                description = "Spicy chicken wings with ranch dip",
                                price = "Rp. 35.000",
                                imageUrl = null
                            )
                        ),
                        onAddToCart = { menuItem ->
                            selectedMenuItem = menuItem
                            selectedMenuDetail = MenuDetail(
                                name = menuItem.name,
                                description = menuItem.description,
                                basePrice = parsePriceFromString(menuItem.price),
                                imageRes = null,
                                subitemCategories = listOf(
                                    SubitemCategoryData(
                                        title = "Size",
                                        options = listOf(
                                            SubitemOption("Regular", "Rp. 0"),
                                            SubitemOption("Large", "Rp. 8.000")
                                        )
                                    ),
                                    SubitemCategoryData(
                                        title = "Sauce",
                                        options = listOf(
                                            SubitemOption("No Sauce", "Rp. 0"),
                                            SubitemOption("Ketchup", "Rp. 0"),
                                            SubitemOption("BBQ Sauce", "Rp. 2.000"),
                                            SubitemOption("Ranch", "Rp. 3.000"),
                                            SubitemOption("Spicy Mayo", "Rp. 3.000")
                                        )
                                    )
                                )
                            )
                            showMenuDetail = true
                        }
                    )
                }
            }
        }

        // Floating "Proceed to Cart" button at the bottom - only shown if cart has items
        if (hasItemsInCart) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 18.dp)
                    .padding(bottom = 23.dp)
            ) {
                PrimaryButton(
                    text = "Proceed to Cart (${cartViewModel?.cartItems?.size ?: 0})",
                    onClick = onProceedToCart
                )
            }
        }

        // Menu Detail Popup
        if (showMenuDetail && selectedMenuDetail != null) {
            MenuDetailPopup(
                menuDetail = selectedMenuDetail!!,
                onDismiss = {
                    showMenuDetail = false
                    selectedMenuItem = null
                    selectedMenuDetail = null
                },
                onAddToCart = { selectedSubitems, quantity ->
                    // Add to cart with selected subitems
                    if (quantity > 0) {
                        cartViewModel?.addToCart(
                            CartItemData(
                                name = selectedMenuDetail!!.name,
                                description = selectedMenuDetail!!.description,
                                basePrice = selectedMenuDetail!!.basePrice,
                                selectedSubitems = selectedSubitems,
                                quantity = quantity,
                                imageRes = selectedMenuDetail!!.imageRes
                            )
                        )
                    }
                    // Note: If quantity is 0, we don't add to cart (user decremented to 0)
                }
            )
        }
    }
}

// Helper function to parse price string
private fun parsePriceFromString(priceString: String): Int {
    return priceString.replace("Rp. ", "").replace(".", "").toIntOrNull() ?: 0
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun MainMenuScreenPreview() {
    PepeSelfOrderingAppTheme {
        MainMenuScreen(
            outlet = "Outlet Brooklyn Tower",
            table = "Table A7B",
            cartViewModel = null,
            onBackPressed = {},
            onSearchQuery = {},
            onProceedToCart = {}
        )
    }
}
