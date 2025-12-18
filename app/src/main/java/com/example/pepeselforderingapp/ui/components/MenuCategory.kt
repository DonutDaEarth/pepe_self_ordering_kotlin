package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangeDivider
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

data class MenuItem(
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String? = null
)

@Composable
fun MenuCategory(
    categoryName: String,
    menuItems: List<MenuItem>,
    onAddToCart: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Category Name
        Text(
            text = categoryName,
            fontFamily = CarterOne,
            fontSize = 20.sp,
            color = BrownDark,
            modifier = Modifier.padding(start = 17.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Divider
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp),
            thickness = 2.dp,
            color = OrangeDivider
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Menu Items
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            menuItems.forEach { menuItem ->
                MenuCard(
                    name = menuItem.name,
                    description = menuItem.description,
                    price = menuItem.price,
                    imageUrl = menuItem.imageUrl,
                    onAddToCart = { onAddToCart(menuItem) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuCategoryPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Burgers Category
            MenuCategory(
                categoryName = "Burgers",
                menuItems = listOf(
                    MenuItem(
                        name = "Pepe Burger",
                        description = "Juicy beef patty with fresh lettuce, tomatoes, and our special sauce",
                        price = "Rp. 670.000"
                    ),
                    MenuItem(
                        name = "Cheese Burger",
                        description = "Classic burger with melted cheese",
                        price = "Rp. 550.000"
                    ),
                    MenuItem(
                        name = "Double Burger",
                        description = "Two beef patties with extra toppings",
                        price = "Rp. 890.000"
                    )
                ),
                onAddToCart = { menuItem ->
                    // Handle add to cart
                }
            )

            // Salads Category
            MenuCategory(
                categoryName = "Salads",
                menuItems = listOf(
                    MenuItem(
                        name = "Caesar Salad",
                        description = "Crispy romaine with parmesan and croutons",
                        price = "Rp. 450.000"
                    ),
                    MenuItem(
                        name = "Greek Salad",
                        description = "Fresh vegetables with feta cheese and olives",
                        price = "Rp. 420.000"
                    )
                ),
                onAddToCart = { menuItem ->
                    // Handle add to cart
                }
            )
        }
    }
}
