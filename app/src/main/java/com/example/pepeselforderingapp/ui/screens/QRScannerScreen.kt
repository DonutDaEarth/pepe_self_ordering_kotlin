package com.example.pepeselforderingapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.R
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

data class ScannedQRData(
    val outlet: String,
    val table: String
)

@Composable
fun QRScannerScreen(
    modifier: Modifier = Modifier,
    onQRScanned: (ScannedQRData) -> Unit = {}
) {
    var scannedData by remember { mutableStateOf<ScannedQRData?>(null) }

    // Simulate QR code scanning after 2 seconds and navigate immediately
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        val data = ScannedQRData(
            outlet = "Outlet Brooklyn Tower",
            table = "Table A7B"
        )
        scannedData = data
        // Navigate after another 1 second
        kotlinx.coroutines.delay(1000)
        onQRScanned(data)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C)) // Dark gray to simulate camera view instead of pure black
    ) {
        // TODO: Add actual camera view here using CameraX

        // Semi-transparent overlay with cutout
        Canvas(modifier = Modifier.fillMaxSize()) {
            val overlayColor = Color(0xFFD9D9D9).copy(alpha = 0.8f)
            val scanBoxSize = 227.dp.toPx()
            val centerX = size.width / 2
            val centerY = size.height / 2
            val scanBoxLeft = centerX - scanBoxSize / 2
            val scanBoxTop = centerY - scanBoxSize / 2

            // Top overlay
            drawRect(
                color = overlayColor,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, scanBoxTop)
            )

            // Bottom overlay
            drawRect(
                color = overlayColor,
                topLeft = Offset(0f, scanBoxTop + scanBoxSize),
                size = Size(size.width, size.height - (scanBoxTop + scanBoxSize))
            )

            // Left overlay
            drawRect(
                color = overlayColor,
                topLeft = Offset(0f, scanBoxTop),
                size = Size(scanBoxLeft, scanBoxSize)
            )

            // Right overlay
            drawRect(
                color = overlayColor,
                topLeft = Offset(scanBoxLeft + scanBoxSize, scanBoxTop),
                size = Size(size.width - (scanBoxLeft + scanBoxSize), scanBoxSize)
            )

            // Draw corner borders
            val cornerSize = 80.dp.toPx()
            val borderThickness = 6.dp.toPx()
            val cornerColor = OrangePrimary

            // Top-left corner
            // Horizontal line
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft, scanBoxTop),
                size = Size(cornerSize, borderThickness)
            )
            // Vertical line
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft, scanBoxTop),
                size = Size(borderThickness, cornerSize)
            )

            // Top-right corner
            // Horizontal line
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft + scanBoxSize - cornerSize, scanBoxTop),
                size = Size(cornerSize, borderThickness)
            )
            // Vertical line
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft + scanBoxSize - borderThickness, scanBoxTop),
                size = Size(borderThickness, cornerSize)
            )

            // Bottom-left corner
            // Vertical line
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft, scanBoxTop + scanBoxSize - cornerSize),
                size = Size(borderThickness, cornerSize)
            )
            // Horizontal line
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft, scanBoxTop + scanBoxSize - borderThickness),
                size = Size(cornerSize, borderThickness)
            )

            // Bottom-right corner
            // Vertical line
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft + scanBoxSize - borderThickness, scanBoxTop + scanBoxSize - cornerSize),
                size = Size(borderThickness, cornerSize)
            )
            // Horizontal line
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft + scanBoxSize - cornerSize, scanBoxTop + scanBoxSize - borderThickness),
                size = Size(cornerSize, borderThickness)
            )
        }

        // Content overlay (logo and button)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Pepe app logo above scan box
            Image(
                painter = painterResource(id = R.drawable.pepe_app_logo),
                contentDescription = "Pepe Logo",
                modifier = Modifier
                    .size(227.dp)
            )

            // Scan box space (227dp) + small gap to button
            Spacer(modifier = Modifier.height(300.dp))

            // Button/Status text below scan box
            val currentScannedData = scannedData
            Box(
                modifier = Modifier
                    .width(260.dp)
                    .then(
                        if (currentScannedData != null) {
                            Modifier.shadow(
                                elevation = 4.dp,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                                spotColor = Color.Black.copy(alpha = 0.3f)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .background(
                        color = OrangePrimary,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (currentScannedData == null) {
                    // Initial state - scan instruction
                    Text(
                        text = "Scan your Table's QR Code",
                        fontFamily = CarterOne,
                        fontSize = 18.sp,
                        color = BrownDark,
                        textAlign = TextAlign.Center
                    )
                } else {
                    // Scanned state - show outlet and table
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentScannedData.outlet,
                            fontFamily = CarterOne,
                            fontSize = 14.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = currentScannedData.table,
                            fontFamily = CarterOne,
                            fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QRScannerScreenPreview() {
    PepeSelfOrderingAppTheme {
        QRScannerScreen(
            onQRScanned = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QRScannerScreenScannedPreview() {
    PepeSelfOrderingAppTheme {
        QRScannerScreen(
            onQRScanned = {}
        )
    }
}
