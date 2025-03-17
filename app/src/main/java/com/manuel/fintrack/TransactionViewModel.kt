package com.manuel.fintrack

import android.os.Build
import android.util.Log

import androidx.annotation.RequiresApi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.manuel.fintrack.bd.TransactionDao

import com.manuel.fintrack.model.ExpenseItem
import com.manuel.fintrack.model.TransactionModel
import com.manuel.fintrack.model.datosBanlance
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@RequiresApi(Build.VERSION_CODES.O)
class TransactionViewModel(private val transactionDao: TransactionDao) : ViewModel() {
    // LiveData para observar cambios en el balance
    private var _balance = MutableLiveData<datosBanlance>()
    var balance: LiveData<datosBanlance> = _balance
    var allTransactions = listOf<TransactionModel>()
    private var currentYear: Int = LocalDate.now().year
    private var currentMonth: Int = LocalDate.now().monthValue


    init {
        loadTransactions()
    }

    class TransactionViewModelFactory(private val transactionDao: TransactionDao) :
        ViewModelProvider.Factory {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(transactionDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadTransactions() {
        viewModelScope.launch {
            // Si transactionDao.getAllTransactions() devuelve un Flow
            transactionDao.getAllTransactions().collect { transactions ->
                allTransactions = transactions
                filterTransactions(currentYear, currentMonth)
                //   _balance.value = calculateBalance(transactions)

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterTransactions(year: Int, month: Int) {
        currentYear = year
        currentMonth = month
        Log.d("- - - >", "$allTransactions")
        viewModelScope.launch {
            val filteredTransactions = allTransactions.filter { transaction ->
                val transactionDate =
                    LocalDate.parse(transaction.date, DateTimeFormatter.ISO_LOCAL_DATE)
                Log.d("años ", transactionDate.year.toString())
                Log.d("mes ", transactionDate.monthValue.toString())
                Log.d("filtoraños ", year.toString())
                Log.d("filtromes ", month.toString())
                transactionDate.year == year && transactionDate.monthValue == month
            }

            Log.d("transacciones", "todas las transacciones: $allTransactions")
            Log.d("TransactionViewModel", "Filtered transactions: $filteredTransactions")

            val newBalance = calculateBalance(filteredTransactions)
            Log.d("BalanceCalculado", "Nuevo balance calculado: $newBalance")

            _balance.value = calculateBalance(filteredTransactions)
        }

    }


    // Esta función ahora recibe una List<TransactionModel>, no un Flow
    private fun calculateBalance(transactions: List<TransactionModel>): datosBanlance {
        val income = transactions.filter { it.type == "income" }.sumOf { it.amount }
        val expenses = transactions.filter { it.type == "expense" }.sumOf { it.amount }
        //crear lista con todas las trasacciones

        val expenseItems = transactions.map { transaction ->
            ExpenseItem(transaction.description, transaction.amount, transaction.id)
        }

        return datosBanlance(
            Nombre = "Manuel",
            balance = income + expenses,
            income = income,
            expenses = expenses,
            expenseItems = expenseItems
        )
    }


    val listincomen = listOf("Ventas", "Sueldo", "Inversiones", "Donaciones", "Premios", "Otros")
    fun getIncomeByCategory(mes: Int, anyo: Int): Flow<Map<String, Double>> {
        return transactionDao.getAllTransactionsByType("income").map { transactions ->
            transactions.filter { transaction ->
            val transactionDate = LocalDate.parse(transaction.date, DateTimeFormatter.ISO_LOCAL_DATE)
            transactionDate.monthValue == mes && transactionDate.year == anyo
        }.groupBy { listincomen[it.category_id] }.mapValues { (_, transactions) ->
                transactions.sumOf { it.amount }
            }
        }
    }

    val listexpense = listOf(
        "Compras Online",
        "Hogar",
        "restaurante",
        "Transporte",
        "Salud",
        "Deporte",
        "Educación",
        "Otros"
    )

    fun getExpenseByCategory(mes: Int, anyo: Int): Flow<Map<String, Double>> {
        return transactionDao.getAllTransactionsByType("expense").map { transactions ->
            transactions.filter { transaction ->
                val transactionDate =
                    LocalDate.parse(transaction.date, DateTimeFormatter.ISO_LOCAL_DATE)
                transactionDate.monthValue == mes && transactionDate.year == anyo
            }.groupBy { listexpense[it.category_id] }.mapValues { (_, transactions) ->
                transactions.sumOf { it.amount * -1 }
            }
        }
    }


}


