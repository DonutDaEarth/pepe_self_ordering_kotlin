package com.example.pepeselforderingapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pepeselforderingapp.data.storage.AuthDataStore
import com.example.pepeselforderingapp.ui.screens.*
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

    var isCheckingAuth by remember { mutableStateOf(true) }
    var startDestination by remember { mutableStateOf(Screen.Register.route) }

    // Check if user is already logged in
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = authDataStore.token.first()
            startDestination = if (token != null) {
                Screen.QRScanner.route
            } else {
                Screen.Register.route
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

    // State to hold cart items across screens
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var tableNumber by remember { mutableStateOf("Table A7B") }
    var outletName by remember { mutableStateOf("Outlet Brooklyn Tower") }

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
                    navController.navigate(Screen.MainMenu.route)
                }
            )
        }

        // Main Menu Screen
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                outlet = outletName,
                table = tableNumber,
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
            CartScreen(
                outlet = outletName,
                table = tableNumber,
                cartItems = cartItems,
                onBackPressed = {
                    navController.popBackStack()
                },
                onProceedToPayment = {
                    navController.navigate(Screen.PaymentSuccess.route)
                },
                onQuantityChange = { index, newQuantity ->
                    cartItems = cartItems.toMutableList().apply {
                        this[index] = this[index].copy(quantity = newQuantity)
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
            val receiptItems = cartItems.map { cartItem ->
                ReceiptItem(
                    name = cartItem.name,
                    selectedSubitems = cartItem.selectedSubitems,
                    totalPrice = cartItem.price,
                    quantity = cartItem.quantity,
                    imageUrl = cartItem.imageUrl
                )
            }

            ReceiptScreen(
                table = tableNumber,
                outlet = outletName,
                receiptItems = receiptItems,
                onMakeAnotherOrder = {
                    cartItems = emptyList()
                    navController.navigate(Screen.QRScanner.route) {
                        popUpTo(Screen.Register.route) { inclusive = false }
                    }
                }
            )
        }
    }
}
