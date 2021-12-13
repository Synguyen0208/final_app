package com.example.shoes.ui.components
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.example.shoes.ui.home.fromHex


@Composable
fun MyTextField(label: String, field: MutableState<String>, type:KeyboardType) {
    val primaryColor = Color.fromHex("#fed700")
    OutlinedTextField(
        value = field.value,
        onValueChange = {
            field.value = it
        },
        label = {
            Text(text = label, color = Color.fromHex("#fed700"))
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = type
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.fromHex("#fed700"),
            unfocusedLabelColor = primaryColor,
            cursorColor = primaryColor,
            unfocusedBorderColor = Color.fromHex("#C9B5D4"),
        )
    )
}