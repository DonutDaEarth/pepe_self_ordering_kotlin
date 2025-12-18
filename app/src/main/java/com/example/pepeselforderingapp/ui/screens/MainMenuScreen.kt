package com.example.pepeselforderingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.data.model.MenuItemData
import com.example.pepeselforderingapp.data.model.SubitemData
import com.example.pepeselforderingapp.data.repository.MenuRepository
import com.example.pepeselforderingapp.ui.components.*
import com.example.pepeselforderingapp.ui.theme.Actor
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CreamBackground
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme
import com.example.pepeselforderingapp.viewmodel.CartItemData
import com.example.pepeselforderingapp.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    outlet: String = "Outlet Brooklyn Tower",
    table: String = "Table A7B",
    outletId: String? = null,
    cartViewModel: CartViewModel? = null,
    onBackPressed: () -> Unit = {},
    onSearchQuery: (String) -> Unit = {},
    onProceedToCart: () -> Unit = {}
) {
    var showMenuDetail by remember { mutableStateOf(false) }
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(null) }
    var selectedMenuDetail by remember { mutableStateOf<MenuDetail?>(null) }

    // State for API data
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var menuCategories by remember { mutableStateOf<List<Pair<String, List<MenuItemData>>>>(emptyList()) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val menuRepository = remember { MenuRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch menu data when screen loads
    LaunchedEffect(outletId) {
        if (outletId != null) {
            isLoading = true
            errorMessage = null
            coroutineScope.launch {
                val result = menuRepository.getOutletMenus(outletId)
                result.onSuccess { response ->
                    if (response.success) {
                        // Group menus by category
                        menuCategories = response.data.map { categoryData ->
                            categoryData.category to categoryData.menus
                        }
                    } else {
                        errorMessage = "Failed to load menu data"
                    }
                    isLoading = false
                }.onFailure { exception ->
                    errorMessage = exception.message ?: "Unknown error occurred"
                    isLoading = false
                }
            }
        } else {
            isLoading = false
            errorMessage = "No outlet ID provided"
        }
    }

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

            // Content area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when {
                    isLoading -> {
                        // Loading state
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    errorMessage != null -> {
                        // Error state
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage ?: "Error loading menu",
                                fontFamily = Actor,
                                fontSize = 16.sp,
                                color = BrownDark,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    menuCategories.isEmpty() -> {
                        // Empty state
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No menu items available",
                                fontFamily = Actor,
                                fontSize = 16.sp,
                                color = BrownDark,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        // Display menu categories
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = if (hasItemsInCart) 80.dp else 16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(11.dp))

                            menuCategories.forEach { (category, menus) ->
                                MenuCategory(
                                    categoryName = category,
                                    menuItems = menus.map { menuData ->
                                        MenuItem(
                                            name = menuData.name,
                                            description = menuData.desc,
                                            price = "Rp. ${formatPrice(menuData.price)}",
                                            imageUrl = menuData.picture_url
                                        )
                                    },
                                    onAddToCart = { menuItem ->
                                        // Find the corresponding menu data
                                        val menuData = menus.find { it.name == menuItem.name }
                                        if (menuData != null) {
                                            selectedMenuItem = menuItem
                                            selectedMenuDetail = MenuDetail(
                                                name = menuData.name,
                                                description = menuData.desc,
                                                basePrice = menuData.price,
                                                imageRes = null,
                                                subitemCategories = groupSubitemsByCategory(menuData.subitems)
                                            )
                                            showMenuDetail = true
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
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
                    showMenuDetail = false
                    selectedMenuItem = null
                    selectedMenuDetail = null
                }
            )
        }
    }
}

// Helper function to group subitems by category
private fun groupSubitemsByCategory(subitems: List<SubitemData>): List<SubitemCategoryData> {
    if (subitems.isEmpty()) return emptyList()

    // Group subitems by their category
    val grouped = subitems.groupBy { it.category }

    return grouped.map { (category, items) ->
        SubitemCategoryData(
            title = category,
            options = items.map { subitem ->
                SubitemOption(
                    name = subitem.name,
                    price = if (subitem.price != null) "Rp. ${formatPrice(subitem.price)}" else "Rp. 0"
                )
            }
        )
    }
}

// Helper function to parse price string
private fun parsePriceFromString(priceString: String): Int {
    return priceString.replace("Rp. ", "").replace(".", "").toIntOrNull() ?: 0
}

// Helper function to format price
private fun formatPrice(price: Int): String {
    return price.toString().reversed().chunked(3).joinToString(".").reversed()
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun MainMenuScreenPreview() {
    PepeSelfOrderingAppTheme {
        MainMenuScreen(
            outlet = "Outlet Brooklyn Tower",
            table = "Table A7B",
            outletId = "1",
            cartViewModel = null,
            onBackPressed = {},
            onSearchQuery = {},
            onProceedToCart = {}
        )
    }
}
