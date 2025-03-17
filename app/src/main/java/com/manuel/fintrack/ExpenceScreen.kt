package com.manuel.fintrack


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.manuel.fintrack.bd.AppDatabase
import com.manuel.fintrack.componentes.DatePickerExample
import com.manuel.fintrack.componentes.ItemsCategory
import com.manuel.fintrack.model.Transaction
import java.time.LocalDate
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope as rememberCoroutineScope
import com.manuel.fintrack.componentes.OutlinedTextFieldCustom
import com.manuel.fintrack.model.Categorias
import com.manuel.fintrack.componentes.BarChartByCategory

// Pantalla de ingresos

// Lista de categorías
val listaCategoriasgastos: List<Categorias> = listOf(
    Categorias(Icons.Filled.AddShoppingCart, "Compras Online"), // selected = false por defecto
    Categorias(Icons.Filled.House, "Hogar"),
    Categorias(Icons.Filled.RestaurantMenu, "restaurante"),
    Categorias(Icons.Filled.DirectionsBus, "Transporte"),
    Categorias(Icons.Filled.LocalHospital, "Salud"),
    Categorias(Icons.Filled.SportsSoccer, "Deporte"),
    Categorias(Icons.Filled.School, "Educación"),
    Categorias(Icons.Filled.AttachMoney, "Otros",true)
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenceScreen(navController: NavController) {
    val context = LocalContext.current
    val bd = AppDatabase.getInstance(context = context)
    val transactionDao = bd.transactionDao()
    val coroutineScope = rememberCoroutineScope()
    val viewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModel.TransactionViewModelFactory(transactionDao)
    )
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val expenseByCategory by viewModel.getExpenseByCategory(selectedDate.value.monthValue,selectedDate.value.year).collectAsState(initial = emptyMap())
    MaterialTheme {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xEDE87A94)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Ingresos",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color(0xEDE87A94),
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {

                Text(
                    text = "Añadir ingreso",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )


                DatePickerExample(selectedDate,Color(0xEDE87A94))



                val concepto = remember { mutableStateOf("") }
                val keyboardOptionsName = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
                OutlinedTextFieldCustom("Concepto", concepto,keyboardOptionsName)

                val cantidad = remember { mutableStateOf("") }
                val keyboardOptionsNumber = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                OutlinedTextFieldCustom("Cantidad", cantidad,keyboardOptionsNumber )




                Text(
                    text = "Selecciona una categoría",
                    style = MaterialTheme.typography.titleLarge ,
                    color = MaterialTheme.colorScheme.primary
                )
                var selectedIndex by remember { mutableStateOf<Int?>(null) }
                var litasdeitems by remember { mutableStateOf(listaCategoriasgastos) }

                LazyRow {
                    itemsIndexed(litasdeitems) { index, categoria ->
                        ItemsCategory(
                            color = Color(0xEDE87A94),
                            categoria = categoria,
                            index = index,
                            onCategorySelected = { clickedIndex ->
                                selectedIndex = if (selectedIndex == clickedIndex) {
                                    null
                                } else {
                                    clickedIndex
                                }
                                // Actualiza la lista
                                litasdeitems = litasdeitems.mapIndexed { currentIndex, currentCategory ->
                                    if (currentIndex == clickedIndex) {
                                        currentCategory.copy(selected = !currentCategory.selected)
                                    } else {
                                        currentCategory.copy(selected = false)
                                    }
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))
                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xEDE87A94),contentColor = Color.Black),
                    onClick = {
                        val cantidadDouble = cantidad.value.toDoubleOrNull()
                        if (cantidadDouble == null) {
                            println("Cantidad no valida")
                        }else{

                            coroutineScope.launch {
                                transactionDao.insert(
                                    Transaction(
                                        id = 0,
                                        user_id = 1,
                                        type = "expense",
                                        description = concepto.value,
                                        amount = cantidadDouble*-1,
                                        date = selectedDate.value.toString(),
                                        category_id = selectedIndex,
                                        is_recurrent = false
                                    )
                                )
                            }

                            navController.popBackStack()
                        }
                    }) {

                    Text("Guardar Ingreso")
                }



                BarChartByCategory(data = expenseByCategory, title = "Ingresos por Categoría")
            }
        }
    }
}






@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ExpenceScreen() {
    ExpenceScreen(navController = rememberNavController())
}



