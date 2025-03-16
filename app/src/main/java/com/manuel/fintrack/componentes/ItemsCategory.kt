package com.manuel.fintrack.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.manuel.fintrack.model.Categorias

@Composable
fun ItemsCategory(
    color: Color,
    categoria: Categorias,
    index: Int,
    onCategorySelected: (Int) -> Unit, // Usa el callback
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onCategorySelected(index) }, // Usa el callback AQUÍ
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Icon(
            categoria.icon,
            categoria.description,
            tint = if (categoria.selected) color else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
        Text(categoria.description)
    }
}