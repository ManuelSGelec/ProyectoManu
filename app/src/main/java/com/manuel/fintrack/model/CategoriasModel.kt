package com.manuel.fintrack.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Categorias(
    val icon: ImageVector,
    val description: String,
    val selected: Boolean = false // Valor por defecto
)