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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.data.repository.AuthRepository
import com.example.pepeselforderingapp.data.repository.AuthResult
import com.example.pepeselforderingapp.data.storage.AuthDataStore
import com.example.pepeselforderingapp.ui.components.Header
import com.example.pepeselforderingapp.ui.components.HeaderMode
import com.example.pepeselforderingapp.ui.components.LabeledTextField
import com.example.pepeselforderingapp.ui.components.PrimaryButton
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onBackPressed: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(AuthDataStore(context)) }
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFEF4E0))
    ) {
        // Header in BASIC mode
        Header(
            mode = HeaderMode.BASIC,
            onBackClick = onBackPressed,
            topPadding = 0.dp
        )

        // Content area - centered
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title text
            Text(
                text = "Login to an existing account",
                fontFamily = CarterOne,
                fontSize = 26.sp,
                color = OrangePrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            )

            Spacer(modifier = Modifier.height(56.dp))

            // E-mail input
            LabeledTextField(
                label = "E-mail",
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Password input
            LabeledTextField(
                label = "Password",
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                modifier = Modifier
                .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            )

            // Error message
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontFamily = CarterOne,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 26.dp)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Login button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp),
                contentAlignment = Alignment.Center
            ) {
                PrimaryButton(
                    text = "Login",
                    onClick = {
                        when {
                            email.isBlank() || password.isBlank() -> {
                                errorMessage = "Email and password are required"
                            }
                            else -> {
                                isLoading = true
                                errorMessage = null
                                coroutineScope.launch {
                                    when (val result = authRepository.login(email, password)) {
                                        is AuthResult.Success -> {
                                            isLoading = false
                                            onLoginSuccess()
                                        }
                                        is AuthResult.Error -> {
                                            isLoading = false
                                            errorMessage = result.message
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = OrangePrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PepeSelfOrderingAppTheme {
        LoginScreen(
            onBackPressed = {},
            onLoginSuccess = {}
        )
    }
}
