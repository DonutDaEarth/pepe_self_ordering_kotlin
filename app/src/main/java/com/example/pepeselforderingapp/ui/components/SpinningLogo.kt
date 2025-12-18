package com.example.pepeselforderingapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pepeselforderingapp.R
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun SpinningLogo(
    modifier: Modifier = Modifier
) {
    // Infinite rotation animation (counter-clockwise)
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -360f, // Negative for counter-clockwise
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .size(219.dp)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape
            )
            .background(
                color = OrangePrimary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inner white circle with inner shadow effect
        Box(
            modifier = Modifier
                .size(201.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
                .drawWithContent {
                    drawContent()
                    // Draw inner shadow using radial gradient (reversed for inner shadow effect)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.1f),
                                Color.Transparent,
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.minDimension / 2f
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Spinning Pepe Cat logo
            Image(
                painter = painterResource(id = R.drawable.pepe_cat),
                contentDescription = "Pepe Cat Logo",
                modifier = Modifier
                    .size(150.dp)
                    .rotate(rotation)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpinningLogoPreview() {
    PepeSelfOrderingAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFEF4E0)),
            contentAlignment = Alignment.Center
        ) {
            SpinningLogo()
        }
    }
}
