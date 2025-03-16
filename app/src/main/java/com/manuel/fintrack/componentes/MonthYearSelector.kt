package com.manuel.fintrack.componentes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Year
import java.time.Month
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import java.time.format.DateTimeFormatter
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthYearSelector(
    onMonthYearSelected: (Int, Int) -> Unit
) {
    val months = remember { Month.values().toList() }
    val years = remember { (2023..Year.now().value +2).toList() }
    var selectedMonth by rememberSaveable { mutableStateOf(Month.from(java.time.LocalDate.now())) }
    var selectedYear by rememberSaveable { mutableStateOf(Year.now()) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selector de Mes
            LazyColumn (
                modifier = Modifier.weight(1f)
            ) {
                items(months) { month ->
                    MonthItem(
                        month = month,
                        isSelected = selectedMonth == month,
                        onMonthSelected = {
                            selectedMonth = it
                        }
                    )
                }
            }

            // Selector de Año
            LazyColumn (
                modifier = Modifier.weight(1f)
            ) {
                items(years) { year ->
                    YearItem(
                        year = year,
                        isSelected = selectedYear.value == year,
                        onYearSelected = {
                            selectedYear = it.let { Year.of(it) }
                        }
                    )
                }
            }
        }

        // Botón para confirmar la selección
        Button(
            onClick = {
                onMonthYearSelected(
                   selectedYear.value,
                    months.indexOf(selectedMonth) + 1
                )
            }
        ) {
            Text("Seleccionar")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthItem(
    month: Month,
    isSelected: Boolean,
    onMonthSelected: (Month) -> Unit
) {
    val localeEs = remember { Locale("es", "ES") }
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM", localeEs) }
    Text(text =  monthFormatter.format(month),
        modifier = Modifier.padding(8.dp).clickable { onMonthSelected(month) },
        if (isSelected) androidx.compose.ui.graphics.Color.Blue else androidx.compose.ui.graphics.Color.Black
        )
}

@Composable
fun YearItem(
    year: Int,
    isSelected: Boolean,
    onYearSelected: (Int) -> Unit
) {

    Text(text = year.toString(),
        modifier = Modifier.padding(8.dp).clickable { onYearSelected(year) },
        if (isSelected) androidx.compose.ui.graphics.Color.Blue else androidx.compose.ui.graphics.Color.Black
        )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun selctmespreview(){
     MonthYearSelector( onMonthYearSelected = { year, month ->
        println("Selected year: $year, month: $month")})

}