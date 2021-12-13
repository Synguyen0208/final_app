
package com.example.foodapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shoes.ui.components.ShoesSurface
import com.example.shoes.ui.theme.ShoesTheme

@Composable
fun FoodCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = Color.White,
    contentColor: Color = Color.Blue,
    border: BorderStroke? = null,
    elevation: Dp = 100.dp,
    content: @Composable () -> Unit
) {
    ShoesSurface(
        modifier = modifier,
        color = color,
        contentColor = contentColor,
        elevation = elevation,
        border = border,
        content = content
    )
}

@Preview("default")
@Composable
private fun CardPreview() {
    ShoesTheme {
        FoodCard {
            Text(text = "Demo", modifier = Modifier.padding(16.dp))
        }
    }
}
