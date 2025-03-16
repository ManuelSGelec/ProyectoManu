package com.manuel.fintrack.bd

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.manuel.fintrack.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FinTrackDatabase : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "fintrack_database" // Nombre de tu base de datos
        ).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        testDatabaseOperations()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun testDatabaseOperations() {
        CoroutineScope(Dispatchers.IO).launch {
            val transactionDao = database.transactionDao()


            val simulatedTransactions = listOf(
                Transaction(1, 1, "income", 1500.00, "Salario mensual", "2025-03-20", 1, false),
                Transaction(2, 1, "expense", -200.00, "Pago de alquiler", "2025-03-21", 2, true),
                Transaction(3, 1, "income", 50.00, "cabra", "2025-03-22", 3, false),
                Transaction(4, 1, "expense", -520.00, "Supermercado", "2025-03-22", 3, false),
                Transaction(5, 1, "income", 550.00, "loteria", "2025-04-22", 3, false),
                Transaction(6, 1, "expense", -506.00, "Supermercado", "2025-04-22", 3, false),
            )

            simulatedTransactions.forEach { transaction ->
                transactionDao.insert(transaction)
            }



          //  transactionDao.insert(transaction)

            // Consultar todas las transacciones
            val transactions = transactionDao.getAllTransactions()
            transactions.collect { list ->
                list.forEach {
                    println("Transaction: $it")
                }
            }
        }
    }
}