package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.R
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

enum class HeaderMode {
    BASIC,
    CART,
    MAIN_MENU
}

@Composable
fun Header(
    mode: HeaderMode,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    topPadding: Dp = 32.dp,
    outletName: String = "",
    tableNumber: String = "",
    searchQuery: String = "",
    onSearchChange: (String) -> Unit = {}
) {
    val headerHeight = when (mode) {
        HeaderMode.BASIC, HeaderMode.CART -> 72.dp + topPadding
        HeaderMode.MAIN_MENU -> 120.dp + topPadding
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
    ) {
        // Shadow layer below header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .offset(y = 4.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        )

        // Main header box with rounded corners
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(
                    color = OrangePrimary,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        ) {
            // Back button - present in all modes, positioned 17dp from top
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "Back",
                tint = Color(0xFF524A42),
                modifier = Modifier
                    .padding(start = 21.dp, top = topPadding + 17.dp)
                    .size(37.dp)
                    .align(Alignment.TopStart)
                    .clickable { onBackClick() }
            )

            // Cart mode - adds outlet and table info, positioned 12dp from top
            if (mode == HeaderMode.CART || mode == HeaderMode.MAIN_MENU) {
                Column(
                    modifier = Modifier
                        .padding(start = 70.dp, top = topPadding + 12.dp)
                        .align(Alignment.TopStart)
                ) {
                    // Table number with fixed height
                    Box(
                        modifier = Modifier.height(23.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = tableNumber,
                            fontFamily = CarterOne,
                            fontSize = 20.sp,
                            color = Color(0xFF524A42)
                        )
                    }

                    // Outlet name
                    Text(
                        text = outletName,
                        fontFamily = CarterOne,
                        fontSize = 11.sp,
                        color = Color(0xFF524A42)
                    )
                }
            }

            // Main menu mode - adds search box with 22dp from bottom
            if (mode == HeaderMode.MAIN_MENU) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .padding(bottom = 22.dp)
                        .height(35.dp)
                        .background(
                            color = Color(0xFFFEF4E0),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp)
                        .align(Alignment.BottomCenter),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.search_menu),
                            contentDescription = "Search",
                            tint = Color.Black.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )

                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchChange,
                            textStyle = TextStyle(
                                fontFamily = CarterOne,
                                fontSize = 13.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search menu...",
                                        fontFamily = CarterOne,
                                        fontSize = 13.sp,
                                        color = Color.Black.copy(alpha = 0.4f)
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Basic mode
            Header(
                mode = HeaderMode.BASIC,
                onBackClick = {},
                topPadding = 24.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Cart mode
            Header(
                mode = HeaderMode.CART,
                onBackClick = {},
                topPadding = 24.dp,
                outletName = "Outlet Brooklyn Tower",
                tableNumber = "Table A7B"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main menu mode
            var searchQuery by remember { mutableStateOf("") }
            Header(
                mode = HeaderMode.MAIN_MENU,
                onBackClick = {},
                topPadding = 24.dp,
                outletName = "Outlet Brooklyn Tower",
                tableNumber = "Table A7B",
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it }
            )
        }
    }
}
