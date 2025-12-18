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
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val menuRepository = remember { MenuRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    // Function to load all menus
    fun loadAllMenus() {
        if (outletId != null) {
            isLoading = true
            errorMessage = null
            isSearching = false
            coroutineScope.launch {
                val result = menuRepository.getOutletMenus(outletId)
                result.onSuccess { response ->
                    if (response.success) {
                        // Group menus by category, filtering out items where is_selling is false
                        menuCategories = response.data.map { categoryData ->
                            categoryData.category to categoryData.menus.filter { it.is_selling }
                        }.filter { it.second.isNotEmpty() }
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

    // Function to search menus
    fun searchMenus(keyword: String) {
        if (outletId != null && keyword.isNotBlank()) {
            isLoading = true
            errorMessage = null
            isSearching = true
            coroutineScope.launch {
                val result = menuRepository.searchMenus(outletId, keyword)
                result.onSuccess { response ->
                    if (response.success) {
                        // Convert search response to menu categories format
                        menuCategories = response.data.map { categoryData ->
                            categoryData.category to categoryData.menus
                                .filter { it.is_selling }
                                .map { searchItem ->
                                    // Convert SearchMenuItem to MenuItemData
                                    MenuItemData(
                                        id = searchItem.id,
                                        m_id = searchItem.m_id,
                                        o_id = searchItem.o_id,
                                        price = searchItem.price,
                                        stock = searchItem.stock,
                                        is_selling = searchItem.is_selling,
                                        sku = searchItem.menu.sku,
                                        name = searchItem.menu.name,
                                        desc = searchItem.menu.desc,
                                        category = searchItem.menu.category,
                                        picture_url = searchItem.menu.picture_url,
                                        picture_path = searchItem.menu.picture_path,
                                        subitems = emptyList() // Search response doesn't include subitems initially
                                    )
                                }
                        }.filter { it.second.isNotEmpty() }
                    } else {
                        errorMessage = "No results found"
                        menuCategories = emptyList()
                    }
                    isLoading = false
                }.onFailure { exception ->
                    errorMessage = exception.message ?: "Search failed"
                    isLoading = false
                }
            }
        } else if (keyword.isBlank()) {
            // If search is cleared, reload all menus
            loadAllMenus()
        }
    }

    // Fetch menu data when screen loads
    LaunchedEffect(outletId) {
        loadAllMenus()
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
            // Header in MAIN_MENU mode with search handling
            Header(
                mode = HeaderMode.MAIN_MENU,
                onBackClick = onBackPressed,
                outletName = outlet,
                tableNumber = table,
                searchQuery = searchQuery,  // Pass the search query state to Header
                onSearchChange = { query ->
                    searchQuery = query
                    // Trigger search when user submits (you'll need to handle this in Header)
                    searchMenus(query)
                }
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
                                        // Find the corresponding menu data by matching both name AND price
                                        // This ensures items with same name but different prices are treated as different items
                                        val menuData = menus.find {
                                            it.name == menuItem.name &&
                                            "Rp. ${formatPrice(it.price)}" == menuItem.price
                                        }
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
            // Find the original menu data to get the m_id
            val originalMenuData = menuCategories
                .flatMap { it.second }
                .find {
                    it.name == selectedMenuDetail!!.name &&
                    it.price == selectedMenuDetail!!.basePrice
                }

            MenuDetailPopup(
                menuDetail = selectedMenuDetail!!,
                onDismiss = {
                    showMenuDetail = false
                    selectedMenuItem = null
                    selectedMenuDetail = null
                },
                onAddToCart = { selectedSubitems, quantity ->
                    // Extract subitem IDs from selected subitems
                    val subitemIds = selectedSubitems.values.map { it.id }

                    // Add to cart with menu ID and subitem IDs
                    cartViewModel?.addToCart(
                        CartItemData(
                            name = selectedMenuDetail!!.name,
                            description = selectedMenuDetail!!.description,
                            basePrice = selectedMenuDetail!!.basePrice,
                            selectedSubitems = selectedSubitems,
                            quantity = quantity,
                            imageRes = selectedMenuDetail!!.imageRes,
                            menuId = originalMenuData?.m_id ?: 0,  // Pass the m_id from API
                            subitemIds = subitemIds  // Pass the subitem IDs
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

    // Filter out subitems where is_selling is false
    val sellingSubitems = subitems.filter { it.is_selling }

    if (sellingSubitems.isEmpty()) return emptyList()

    // Group subitems by their category
    val grouped = sellingSubitems.groupBy { it.category }

    return grouped.map { (category, items) ->
        SubitemCategoryData(
            title = category,
            options = items.map { subitem ->
                SubitemOption(
                    name = subitem.name,
                    price = if (subitem.price != null) "Rp. ${formatPrice(subitem.price)}" else "Rp. 0",
                    id = subitem.id  // Include the subitem ID from API
                )
            }
        )
    }.filter { it.options.isNotEmpty() }  // Remove categories with no available options
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
