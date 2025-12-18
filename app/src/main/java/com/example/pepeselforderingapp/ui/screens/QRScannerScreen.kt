package com.example.pepeselforderingapp.ui.screens

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pepeselforderingapp.R
import com.example.pepeselforderingapp.data.storage.AuthDataStore
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

data class ScannedQRData(
    val id: String,
    val outlet: String,
    val table: String
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRScannerScreen(
    modifier: Modifier = Modifier,
    onQRScanned: (ScannedQRData) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val authDataStore = remember { AuthDataStore(context) }
    val coroutineScope = rememberCoroutineScope()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var scannedData by remember { mutableStateOf<ScannedQRData?>(null) }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
    ) {
        if (cameraPermissionState.status.isGranted) {
            CameraPreview(
                onQRCodeScanned = { qrData ->
                    if (scannedData == null) {
                        scannedData = qrData
                    }
                }
            )
        } else {
            // Show permission denied message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Camera permission is required\nto scan QR codes",
                    fontFamily = CarterOne,
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

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
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft, scanBoxTop),
                size = Size(cornerSize, borderThickness)
            )
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft, scanBoxTop),
                size = Size(borderThickness, cornerSize)
            )

            // Top-right corner
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft + scanBoxSize - cornerSize, scanBoxTop),
                size = Size(cornerSize, borderThickness)
            )
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft + scanBoxSize - borderThickness, scanBoxTop),
                size = Size(borderThickness, cornerSize)
            )

            // Bottom-left corner
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft, scanBoxTop + scanBoxSize - cornerSize),
                size = Size(borderThickness, cornerSize)
            )
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft, scanBoxTop + scanBoxSize - borderThickness),
                size = Size(cornerSize, borderThickness)
            )

            // Bottom-right corner
            drawRect(
                color = cornerColor,
                topLeft = Offset(scanBoxLeft + scanBoxSize - borderThickness, scanBoxTop + scanBoxSize - cornerSize),
                size = Size(borderThickness, cornerSize)
            )
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
                    .padding(vertical = 8.dp)
                    .then(
                        if (currentScannedData != null) {
                            Modifier.clickable {
                                // Navigate to menu when button is clicked
                                onQRScanned(currentScannedData)
                            }
                        } else {
                            Modifier
                        }
                    ),
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

            Spacer(modifier = Modifier.height(13.dp))

            // Logout button
            com.example.pepeselforderingapp.ui.components.TextButton(
                text = "Log out of Current Account",
                onClick = {
                    coroutineScope.launch {
                        authDataStore.clearAuthData()
                        onLogout()
                    }
                }
            )
        }
    }
}

@Composable
fun CameraPreview(
    onQRCodeScanned: (ScannedQRData) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }
    val barcodeScanner = remember { BarcodeScanning.getClient() }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val preview = CameraPreview.Builder().build()
            val selector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(executor) { imageProxy ->
                processImageProxy(barcodeScanner, imageProxy, onQRCodeScanned)
            }

            try {
                cameraProviderFuture.get().unbindAll()
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("CameraPreview", "Error binding camera", e)
            }

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    barcodeScanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    imageProxy: ImageProxy,
    onQRCodeScanned: (ScannedQRData) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    when (barcode.valueType) {
                        Barcode.TYPE_TEXT, Barcode.TYPE_URL -> {
                            barcode.rawValue?.let { qrContent ->
                                // Parse QR code content
                                // Expected format: "outlet:Brooklyn Tower,table:A7B"
                                val data = parseQRContent(qrContent)
                                if (data != null) {
                                    onQRCodeScanned(data)
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("QRScanner", "Barcode scanning failed", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}

private fun parseQRContent(qrContent: String): ScannedQRData? {
    return try {
        // First, try to decode hex-encoded JSON
        // Expected format: "7B226964223A2231222C226F75746C6574223A224F75746C65742042726F6F6B6C796E20546F776572222C227461626C65223A22413742227D"
        val decodedJson = if (qrContent.matches(Regex("^[0-9A-Fa-f]+$"))) {
            // It's a hex string, decode it
            hexToString(qrContent)
        } else {
            // It's already a regular string
            qrContent
        }

        Log.d("QRScanner", "Decoded QR content: $decodedJson")

        // Parse JSON: {"id":"1","outlet":"Outlet Brooklyn Tower","table":"A7B"}
        val json = org.json.JSONObject(decodedJson)
        val id = json.getString("id")
        val outlet = json.getString("outlet")
        val table = json.getString("table")

        ScannedQRData(id, outlet, "Table $table")
    } catch (e: Exception) {
        Log.e("QRScanner", "Failed to parse QR content: ${e.message}")
        // Fallback: return null to indicate invalid QR code
        null
    }
}

// Helper function to convert hex string to regular string
private fun hexToString(hex: String): String {
    val result = StringBuilder()
    var i = 0
    while (i < hex.length) {
        val hexByte = hex.substring(i, i + 2)
        val decimal = hexByte.toInt(16)
        result.append(decimal.toChar())
        i += 2
    }
    return result.toString()
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
