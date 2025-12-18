package com.example.pepeselforderingapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pepeselforderingapp.ui.theme.Actor
import com.example.pepeselforderingapp.ui.theme.BrownDark
import com.example.pepeselforderingapp.ui.theme.OrangeDivider
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

@Composable
fun SubitemCategory(
    title: String,
    options: List<SubitemOption>,
    selectedOption: SubitemOption?,
    onOptionSelected: (SubitemOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = OrangeDivider
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = title,
            fontFamily = Actor,
            fontSize = 16.sp,
            color = BrownDark,
            modifier = Modifier.padding(start = 6.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Options
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                SubitemSelection(
                    name = option.name,
                    price = option.price,
                    selected = option == selectedOption,
                    onSelect = { onOptionSelected(option) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubitemCategoryPreview() {
    PepeSelfOrderingAppTheme {
        var selectedMilk by remember {
            mutableStateOf<SubitemOption?>(
                SubitemOption("Almond Milk", "Rp. 15.000")
            )
        }
        var selectedSize by remember {
            mutableStateOf<SubitemOption?>(
                SubitemOption("Medium", "Rp. 0")
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SubitemCategory(
                title = "Milk Choices",
                options = listOf(
                    SubitemOption("Almond Milk", "Rp. 15.000"),
                    SubitemOption("Oat Milk", "Rp. 12.000"),
                    SubitemOption("Soy Milk", "Rp. 10.000"),
                    SubitemOption("Regular Milk", "Rp. 0")
                ),
                selectedOption = selectedMilk,
                onOptionSelected = { selectedMilk = it }
            )

            SubitemCategory(
                title = "Size",
                options = listOf(
                    SubitemOption("Small", "- Rp. 5.000"),
                    SubitemOption("Medium", "Rp. 0"),
                    SubitemOption("Large", "Rp. 8.000"),
                    SubitemOption("Extra Large", "Rp. 15.000")
                ),
                selectedOption = selectedSize,
                onOptionSelected = { selectedSize = it }
            )
        }
    }
}
