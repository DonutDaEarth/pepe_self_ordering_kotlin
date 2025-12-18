package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.theme.BeigeLight
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true
) {
    Column(
        modifier = modifier
    ) {
        // Label
        Text(
            text = label,
            fontSize = 15.sp,
            fontFamily = CarterOne,
            color = OrangePrimary,
            modifier = Modifier
                .padding(start = 8.dp, bottom = 2.dp)
        )

        // TextField with inner shadow effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            // Outer container with drop shadow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black.copy(alpha = 0.4f),
                        ambientColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = BeigeLight,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                // Inner shadow effect - dark overlay at top
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                )

                // TextField content
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        fontFamily = CarterOne,
                        fontSize = 20.sp,
                        color = Color.Black
                    ),
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    singleLine = singleLine,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty() && placeholder.isNotEmpty()) {
                                Text(
                                    text = placeholder,
                                    fontSize = 20.sp,
                                    fontFamily = CarterOne,
                                    color = Color.Black.copy(alpha = 0.4f)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LabeledTextFieldPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Empty state with placeholder
            var emptyValue by remember { mutableStateOf("") }
            LabeledTextField(
                value = emptyValue,
                onValueChange = { emptyValue = it },
                label = "Email",
                placeholder = "Enter your email"
            )

            // Filled state
            var filledValue by remember { mutableStateOf("john.doe@example.com") }
            LabeledTextField(
                value = filledValue,
                onValueChange = { filledValue = it },
                label = "Username",
                placeholder = "Enter username"
            )
        }
    }
}
