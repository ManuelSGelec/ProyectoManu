package com.manuel.fintrack.componentes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate


@Composable
fun  ItemMenssage(
                    titulo: String = "Titulo",
                   message: String,
                  timestamp: String,
                  modifier: Modifier = Modifier){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(8.dp),

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = titulo,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(8.dp)
                    .height(95.dp)
                    .verticalScroll(rememberScrollState()),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.End)
            )
        }

    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewItemMessage(){
    ItemMenssage("assdasdasda","dasdasdasdasdasdasd" +
            "sssssssssssssssssssssssssssssssssssssssss" +
            "ffffffffffffffffffffffffffffffffffff" +
            "asfghfghfghfghfghfghfghfghfghfghffghfghfgdavxcvxcvxcvxc" +
            "sdagfgdfgdfgdsdsdfsdfawdfasdgdfgdgsfgdsfgdsgsdgfgdfgdfgdfgdf" +
            "gdffghgdfgdfgdfgdfgdfgdfgdfgdfgdsdasdasdasd",LocalDate.now().toString())
}
