package com.manuel.fintrack

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.mikephil.charting.data.PieEntry
import com.manuel.fintrack.bd.AppDatabase
import com.manuel.fintrack.componentes.BalanceCard
import com.manuel.fintrack.model.Transaction
import java.text.NumberFormat
import java.util.Locale

//graficos

import com.manuel.fintrack.componentes.ComposePieChart
import com.manuel.fintrack.componentes.TransactionItem
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.manuel.fintrack.componentes.MonthYearSelector
import com.manuel.fintrack.model.datosBanlance

import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(navController: NavController, id: Int) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val context = LocalContext.current
    val bd = AppDatabase.getInstance(context = context)
    val transactionDao by remember { mutableStateOf(bd.transactionDao()) }
    val viewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModel.TransactionViewModelFactory(transactionDao)
    )
    // Variables para almacenar el mes y año seleccionados
    var selectedYear by rememberSaveable { mutableStateOf(LocalDate.now().year) }
    var selectedMonth by rememberSaveable { mutableStateOf(LocalDate.now().monthValue) }

    // Variable para controlar la visibilidad del diálogo
    var showDialog by rememberSaveable { mutableStateOf(false) }

// Observa el estado del balance
    val balanceState by viewModel.balance.observeAsState()
    Log.d("BalanceState", "BalanceState: $balanceState")
    // Observa el estado del balance
    var username = "Manuel" // O obtén esto de tus datos de usuario
    var balance = balanceState?.balance ?: 0.0 // Proporciona un valor predeterminado
    var income = balanceState?.income ?: 0.0 // Proporciona un valor predeterminado
    var expenses = balanceState?.expenses ?: 0.0 // Proporciona un valor predeterminado
    var expenseItems = balanceState?.expenseItems ?: emptyList() // Proporciona un valor predeterminado

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = "Hola, $username",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "Fecha seleccionada: $selectedYear - $selectedMonth",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { showDialog = true },
                textAlign = TextAlign.Center,
                color = Color(0xFF075490),
                fontWeight = FontWeight.Bold

            )

            // Diálogo para seleccionar mes y año

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Seleccionar mes y año") },
                    text = {
                        MonthYearSelector(onMonthYearSelected = { year, month ->
                            Log.d("FechaSeleccionada", "Año: $year, Mes: $month")
                            selectedYear = year
                            selectedMonth = month
                            viewModel.filterTransactions(year, month)
                            showDialog = false // Cerrar el diálogo al seleccionar
                        })
                    },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }


            // Balance total
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xEDA4C7E3))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Balance total",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = currencyFormatter.format(balance),
                        style = MaterialTheme.typography.headlineLarge,
                        color = if (balance >= 0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error
                    )
                }
            }

            // Gráfico de ingresos/gastos
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                ComposePieChart(
                    modifier = Modifier.fillMaxSize(),
                    entries = listOf(
                        PieEntry(income.toFloat(), "Ingresos"),
                        PieEntry(expenses.toFloat() * -1, "Gastos")
                    ),
                    colors = listOf(Color(0xFF95CCB3), Color(0xE4E25F5F))
                )
            }

            // Tarjetas de ingresos y gastos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BalanceCard(
                    title = "Ingresos",
                    amount = income,
                    isIncome = true,
                    modifier = Modifier.weight(1f)
                )
                BalanceCard(
                    title = "Gastos",
                    amount = expenses,
                    isIncome = false,
                    modifier = Modifier.weight(1f).height(80.dp)
                )
            }

            // Lista de transacciones
            Card(
                modifier = Modifier.fillMaxWidth().heightIn(max = 230.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    Text(
                        text = "Últimas transacciones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 7.dp)
                    )

                    HorizontalDivider()

                    if (expenseItems.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay transacciones registradas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(expenseItems) { item  ->
                                TransactionItem(
                                    item.description,
                                    item.amount,
                                    item.id
                                )
                            }
                        }
                    }
                }
            }

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("income") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Ingresos")
                }

                Button(
                    onClick = { navController.navigate("expense") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Gastos")
                }
            }
        }
    }
}





@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)

@Composable
fun BalanceScreenPreview() {
    BalanceScreen(
        rememberNavController(),
        id = 1
    )
}
