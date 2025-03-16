package com.manuel.fintrack.model

data class datosBanlance(
    val Nombre: String,
    val balance: Double,
    val income: Double,
    val expenses: Double,
    val expenseItems: List<ExpenseItem>
)
