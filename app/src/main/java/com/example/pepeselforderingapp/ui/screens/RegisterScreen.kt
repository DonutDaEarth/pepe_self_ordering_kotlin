package com.example.pepeselforderingapp.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.R
import com.example.pepeselforderingapp.data.repository.AuthRepository
import com.example.pepeselforderingapp.data.repository.AuthResult
import com.example.pepeselforderingapp.data.storage.AuthDataStore
import com.example.pepeselforderingapp.ui.components.LabeledTextField
import com.example.pepeselforderingapp.ui.components.PrimaryButton
import com.example.pepeselforderingapp.ui.components.TextButton
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(AuthDataStore(context)) }
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFEF4E0))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.pepe_app_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(246.dp)
        )

        // Name input (Note: API doesn't use name, but kept for UI)
        LabeledTextField(
            label = "Name",
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

        // Confirmed Password input
        LabeledTextField(
            label = "Confirmed Password",
            value = confirmedPassword,
            onValueChange = {
                confirmedPassword = it
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

        Spacer(modifier = Modifier.height(81.dp))

        // Already have an account text button
        TextButton(
            text = "Already have an account?",
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Register button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 19.dp),
            contentAlignment = Alignment.Center
        ) {
            PrimaryButton(
                text = "Register",
                onClick = {
                    // Validation
                    when {
                        email.isBlank() || password.isBlank() -> {
                            errorMessage = "Email and password are required"
                        }
                        password != confirmedPassword -> {
                            errorMessage = "Passwords do not match"
                        }
                        password.length < 6 -> {
                            errorMessage = "Password must be at least 6 characters"
                        }
                        else -> {
                            isLoading = true
                            errorMessage = null
                            coroutineScope.launch {
                                when (val result = authRepository.register(email, password)) {
                                    is AuthResult.Success -> {
                                        isLoading = false
                                        onRegisterSuccess()
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

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    PepeSelfOrderingAppTheme {
        RegisterScreen(
            onLoginClick = {},
            onRegisterSuccess = {}
        )
    }
}
