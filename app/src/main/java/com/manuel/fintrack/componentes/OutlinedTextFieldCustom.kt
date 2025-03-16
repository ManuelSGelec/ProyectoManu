package com.manuel.fintrack.componentes

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun OutlinedTextFieldCustom(texto: String, text: MutableState<String>, typekeyboardOption: KeyboardOptions) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text("$texto") },
        shape = MaterialTheme.shapes.extraLarge,
        keyboardOptions =typekeyboardOption
    )
}
