package com.manuel.fintrack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val user_id: Int,
    val type: String,
    val amount: Double,
    val description: String,
    val date: String,
    val category_id: Int?,
    val is_recurrent: Boolean
)

data class TransactionModel(
    val id: Int = 0,
    val user_id: Int,
    val type: String,
    val amount: Double,
    val description: String,
    val date: String,
    val category_id: Int,
    val is_recurrent: Boolean
)
