package com.manuel.fintrack.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manuel.fintrack.bd.AppDatabase
import java.text.NumberFormat
import java.util.Locale
import com.manuel.fintrack.componentes.AlertDialogCustom
import kotlinx.coroutines.launch

@Composable
fun TransactionItem(name: String, amount: Double,id: Int) {
    val context = LocalContext.current
    val bd = AppDatabase.getInstance(context = context)
    val transactionDao = bd.transactionDao()
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    var showDialog by remember { mutableStateOf(false) }
    val isExpense = amount < 0
    val idTransaction = id
    val amountColor = if (isExpense)
        MaterialTheme.colorScheme.error
    else
       Color(0xFF61AA88)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                showDialog = true
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = currencyFormatter.format(amount),
                style = MaterialTheme.typography.bodyLarge,
                color = amountColor,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
    val coroutineScope = rememberCoroutineScope()
    if(showDialog) {
        AlertDialogCustom(
            icon = Icons.Filled.Delete,
            dialogTitle = "Eliminar transacción",
            dialogText = "¿Estás seguro de eliminar esta transacción?",
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                coroutineScope.launch{
                    transactionDao.deleteTransaction(idTransaction)
                    showDialog = false
                }


            },
            description = "Eliminar"
        )
    }

}


@Composable
fun BalanceCard(title: String, amount: Double, isIncome: Boolean, modifier: Modifier = Modifier) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val backgroundColor = if (isIncome)
        Color( 0xFF95CCB3)
    else
        Color(0xE4E25F5F)

    val contentColor = if (isIncome)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSecondaryContainer

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isIncome) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown ,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor
            )


            Text(
                text = currencyFormatter.format(amount),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

@Preview 
@Composable
fun viewejemplo (){
    BalanceCard(amount = 100.0, isIncome = true, title = "Ingresos")
}