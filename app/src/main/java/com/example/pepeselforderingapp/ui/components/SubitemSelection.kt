package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.CarterOne
import com.example.pepeselforderingapp.ui.theme.OrangePrimary
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

data class SubitemOption(
    val name: String,
    val price: String,
    val id: Int = 0  // Add subitem ID for order creation
)

@Composable
fun SubitemSelection(
    name: String,
    price: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Radio Button
        RadioButton(
            selected = selected,
            onClick = onSelect,
            modifier = Modifier.size(18.dp),
            colors = RadioButtonDefaults.colors(
                selectedColor = OrangePrimary,
                unselectedColor = BrownDark
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Name
        Text(
            text = name,
            fontFamily = CarterOne,
            fontSize = 15.sp,
            color = BrownDark
        )

        // Price (only show if not 0)
        if (price != "Rp. 0") {
            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "+ $price",
                fontFamily = CarterOne,
                fontSize = 12.sp,
                color = OrangePrimary.copy(alpha = 0.75f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubitemSelectionPreview() {
    PepeSelfOrderingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SubitemSelection(
                name = "Almond Milk",
                price = "Rp. 15.000",
                selected = true,
                onSelect = {}
            )

            SubitemSelection(
                name = "Oat Milk",
                price = "Rp. 12.000",
                selected = false,
                onSelect = {}
            )

            SubitemSelection(
                name = "Soy Milk",
                price = "Rp. 10.000",
                selected = false,
                onSelect = {}
            )
        }
    }
}
