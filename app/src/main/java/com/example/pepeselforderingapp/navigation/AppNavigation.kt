package com.example.pepeselforderingapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pepeselforderingapp.data.repository.OrderRepository
import com.example.pepeselforderingapp.data.storage.AuthDataStore
import com.example.pepeselforderingapp.ui.screens.*
import com.example.pepeselforderingapp.viewmodel.CartViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Register : Screen("register")
    object Login : Screen("login")
    object QRScanner : Screen("qr_scanner")
    object MainMenu : Screen("main_menu")
    object Cart : Screen("cart")
    object PaymentSuccess : Screen("payment_success")
    object Receipt : Screen("receipt")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authDataStore: AuthDataStore = remember { AuthDataStore(context) }
    val coroutineScope = rememberCoroutineScope()

    // Create CartViewModel instance that persists across navigation
    val cartViewModel: CartViewModel = viewModel()

    var isCheckingAuth by remember { mutableStateOf(true) }
    var startDestination by remember { mutableStateOf(Screen.Register.route) }

    // Check if user is already logged in and if there's a pending receipt
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = authDataStore.token.first()
            val lastOrderUid = authDataStore.lastOrderUid.first()

            startDestination = when {
                token == null -> Screen.Register.route
                lastOrderUid != null -> Screen.Receipt.route  // Resume with receipt if exists
                else -> Screen.QRScanner.route
            }
            isCheckingAuth = false
        }
    }

    // Don't show anything while checking auth
    if (isCheckingAuth) {
        // Show a simple loading screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFEF4E0)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = com.example.pepeselforderingapp.ui.theme.OrangePrimary
            )
        }
        return
    }

    // State to hold table, outlet, user info, and order UID
    var tableNumber by remember { mutableStateOf("Table A7B") }
    var outletName by remember { mutableStateOf("Outlet Brooklyn Tower") }
    var outletId by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<Int?>(null) }
    var currentOrderUid by remember { mutableStateOf<String?>(null) }

    // Load user ID and last order UID on start
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userId = authDataStore.userId.first()
            currentOrderUid = authDataStore.lastOrderUid.first()
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Register Screen
        composable(Screen.Register.route) {
            RegisterScreen(
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.QRScanner.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    navController.navigate(Screen.QRScanner.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // QR Scanner Screen
        composable(Screen.QRScanner.route) {
            QRScannerScreen(
                onQRScanned = { scannedData ->
                    tableNumber = scannedData.table
                    outletName = scannedData.outlet
                    outletId = scannedData.id  // Store the outlet ID
                    // Clear cart when scanning new table
                    cartViewModel.clearCart()
                    navController.navigate(Screen.MainMenu.route)
                },
                onLogout = {
                    // Clear cart on logout
                    cartViewModel.clearCart()
                    navController.navigate(Screen.Register.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Main Menu Screen
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                outlet = outletName,
                table = tableNumber,
                outletId = outletId,  // Pass the outlet ID
                cartViewModel = cartViewModel,
                onBackPressed = {
                    navController.popBackStack()
                },
                onProceedToCart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        // Cart Screen
        composable(Screen.Cart.route) {
            val orderRepository = remember { OrderRepository(context) }
            var isCreatingOrder by remember { mutableStateOf(false) }
            var orderError by remember { mutableStateOf<String?>(null) }

            CartScreen(
                outlet = outletName,
                table = tableNumber,
                cartViewModel = cartViewModel,
                onBackPressed = {
                    navController.popBackStack()
                },
                onProceedToPayment = {
                    // Create order and navigate to payment success
                    coroutineScope.launch {
                        isCreatingOrder = true
                        orderError = null

                        // Build order request from cart items
                        val orderItems = cartViewModel.cartItems.map { cartItem ->
                            com.example.pepeselforderingapp.data.model.OrderItemRequest(
                                menu_id = cartItem.menuId,
                                quantity = cartItem.quantity,
                                subitems = cartItem.subitemIds.map { subitemId ->
                                    com.example.pepeselforderingapp.data.model.SubitemRequest(
                                        menu_id = subitemId,
                                        quantity = 1
                                    )
                                }
                            )
                        }

                        val createOrderRequest = com.example.pepeselforderingapp.data.model.CreateOrderRequest(
                            outlet_id = outletId?.toIntOrNull() ?: 1,
                            table_no = tableNumber,
                            user_id = userId ?: 1,
                            order_item = orderItems
                        )

                        val result = orderRepository.createOrder(createOrderRequest)
                        result.onSuccess { response ->
                            if (response.success) {
                                // Save order UID for receipt persistence
                                currentOrderUid = response.data.uid
                                authDataStore.saveLastOrderUid(response.data.uid)

                                // Navigate to payment success
                                navController.navigate(Screen.PaymentSuccess.route)
                            } else {
                                orderError = "Failed to create order"
                            }
                        }.onFailure { exception ->
                            orderError = exception.message ?: "Unknown error"
                        }

                        isCreatingOrder = false
                    }
                }
            )
        }

        // Payment Success Screen
        composable(Screen.PaymentSuccess.route) {
            PaymentSuccessScreen(
                onNavigateToReceipt = {
                    navController.navigate(Screen.Receipt.route) {
                        popUpTo(Screen.MainMenu.route) { inclusive = true }
                    }
                }
            )
        }

        // Receipt Screen
        composable(Screen.Receipt.route) {
            val orderRepository = remember { OrderRepository(context) }
            var isLoadingReceipt by remember { mutableStateOf(true) }
            var receiptData by remember { mutableStateOf<com.example.pepeselforderingapp.data.model.TrackOrderData?>(null) }
            var receiptError by remember { mutableStateOf<String?>(null) }

            // Load receipt data from API
            LaunchedEffect(currentOrderUid) {
                if (currentOrderUid != null) {
                    coroutineScope.launch {
                        isLoadingReceipt = true

                        // Fetch order details
                        val orderResult = orderRepository.trackOrder(currentOrderUid!!)
                        orderResult.onSuccess { response ->
                            if (response.success) {
                                receiptData = response.data
                            } else {
                                receiptError = "Failed to load receipt"
                            }
                        }.onFailure { exception ->
                            receiptError = exception.message ?: "Unknown error"
                        }

                        isLoadingReceipt = false
                    }
                }
            }

            if (isLoadingReceipt) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (receiptData != null) {
                // Map API data to receipt items - names are now included in the response
                val receiptItems = receiptData!!.or_order_item.items.map { item ->
                    // Get subitem names from the item's subitems array
                    val subitemNames = item.subitems.joinToString(", ") { it.name }

                    ReceiptItem(
                        name = item.name,
                        selectedSubitems = subitemNames,
                        totalPrice = "Rp. ${formatPrice(item.total)}",
                        quantity = item.quantity,
                        imageUrl = null
                    )
                }

                ReceiptScreen(
                    table = receiptData!!.table_no,
                    outlet = receiptData!!.outlet.name,
                    receiptItems = receiptItems,
                    onMakeAnotherOrder = {
                        // Clear cart and last order UID
                        cartViewModel.clearCart()
                        coroutineScope.launch {
                            authDataStore.clearLastOrderUid()
                        }
                        currentOrderUid = null

                        navController.navigate(Screen.QRScanner.route) {
                            popUpTo(Screen.Register.route) { inclusive = false }
                        }
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = receiptError ?: "Failed to load receipt",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    return price.toString().reversed().chunked(3).joinToString(".").reversed()
}
