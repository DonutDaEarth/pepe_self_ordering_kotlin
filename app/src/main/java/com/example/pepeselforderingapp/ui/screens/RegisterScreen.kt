package com.example.pepeselforderingapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pepeselforderingapp.R
import com.example.pepeselforderingapp.ui.components.LabeledTextField
import com.example.pepeselforderingapp.ui.components.PrimaryButton
import com.example.pepeselforderingapp.ui.components.TextButton
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }

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

        // Name input
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
            onValueChange = { email = it },
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

        Spacer(modifier = Modifier.height(24.dp))

        // Confirmed Password input
        LabeledTextField(
            label = "Confirmed Password",
            value = confirmedPassword,
            onValueChange = { confirmedPassword = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
        )

        Spacer(modifier = Modifier.height(81.dp))

        // Already have an account text button
        TextButton(
            text = "Already have an account?",
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Register button
        PrimaryButton(
            text = "Register",
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 19.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    PepeSelfOrderingAppTheme {
        RegisterScreen(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}
