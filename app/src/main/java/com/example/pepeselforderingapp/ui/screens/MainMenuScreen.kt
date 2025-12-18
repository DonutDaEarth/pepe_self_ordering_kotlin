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

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    outlet: String = "Outlet Brooklyn Tower",
    table: String = "Table A7B",
    onBackPressed: () -> Unit = {},
    onSearchQuery: (String) -> Unit = {},
    onProceedToCart: () -> Unit = {}
) {
    var showMenuDetail by remember { mutableStateOf(false) }
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(null) }

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
                        .padding(bottom = 80.dp)
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
                            showMenuDetail = true
                        }
                    )
                }
            }
        }

        // Floating "Proceed to Cart" button at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 18.dp)
                .padding(bottom = 23.dp)
        ) {
            PrimaryButton(
                text = "Proceed to Cart",
                onClick = onProceedToCart
            )
        }

        // Menu Detail Popup
        if (showMenuDetail && selectedMenuItem != null) {
            MenuDetailPopup(
                menuDetail = MenuDetail(
                    name = selectedMenuItem!!.name,
                    description = selectedMenuItem!!.description,
                    basePrice = parsePriceFromString(selectedMenuItem!!.price),
                    imageRes = null,
                    subitemCategories = listOf(
                        SubitemCategoryData(
                            title = "Milk Choices",
                            options = listOf(
                                SubitemOption("Regular Milk", "Rp. 0"),
                                SubitemOption("Almond Milk", "Rp. 5.000"),
                                SubitemOption("Soy Milk", "Rp. 3.000")
                            )
                        ),
                        SubitemCategoryData(
                            title = "Size",
                            options = listOf(
                                SubitemOption("Regular", "Rp. 0"),
                                SubitemOption("Large", "Rp. 10.000")
                            )
                        )
                    )
                ),
                onDismiss = {
                    showMenuDetail = false
                    selectedMenuItem = null
                },
                onAddToCart = {
                    // TODO: Add to cart logic
                    showMenuDetail = false
                    selectedMenuItem = null
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
            onBackPressed = {},
            onSearchQuery = {},
            onProceedToCart = {}
        )
    }
}
