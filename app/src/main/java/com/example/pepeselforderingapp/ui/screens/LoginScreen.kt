package com.example.pepeselforderingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.components.Header
import com.example.pepeselforderingapp.ui.components.HeaderMode
import com.example.pepeselforderingapp.ui.components.LabeledTextField
import com.example.pepeselforderingapp.ui.components.PrimaryButton
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun LoginScreen(
    onBackPressed: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var nameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

            // Name / E-mail input
            LabeledTextField(
                label = "Name / E-mail",
                value = nameOrEmail,
                onValueChange = { nameOrEmail = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Password input
            LabeledTextField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Login button
            PrimaryButton(
                text = "Login",
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PepeSelfOrderingAppTheme {
        LoginScreen(
            onBackPressed = {},
            onLoginClick = {}
        )
    }
}
