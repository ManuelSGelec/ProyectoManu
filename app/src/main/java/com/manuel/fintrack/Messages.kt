package com.manuel.fintrack

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.manuel.fintrack.componentes.ItemMenssage
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)

fun Messages(navController: NavController) {

    val listamensage= listOf(
        MessagesModel ("mensaje1","sjdksjdfjbsdkjfbbfkjsdbfkjsbdfkjsbdkfjbk", LocalDate.now().toString()),
        MessagesModel ("mensaje2","sjdksjdfjbsdkjfbskdjbfksjdbfkjsdbfssdgsdfkjsbdfkjsbdkfjbk", LocalDate.now().toString()),
        MessagesModel ("mensaje3","sjdksjdfjbsdkjfbskdjbfksjdbfkjsdbfkjsbdfkjsbdkfjbk", LocalDate.now().toString()),
        MessagesModel ("mensaje4","sjdksjdfjbsdkjfbskdjbfksjsdgsdgsdgsdgsdgsdgsdbfkjsdbfkjsbdfkjsbdkfjbk", LocalDate.now().toString()),
        MessagesModel ("mensaje5","sjdksjdfjbsdkjfbdgsdgsdgdgsdgsdgsdgsdgsdgsdgsdgsdsdgskdjbfksjdbfkjsdbfkjsbdfkjsbdkfjbk", LocalDate.now().toString())
    )

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    val username = "manu"
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
        })
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(listamensage) { menssage ->
                    ItemMenssage(menssage.titulo,menssage.cuerpo,menssage.fecha)
                }

            }
        }
    }



}

data class MessagesModel (
    val titulo :String ,
    val cuerpo :String ,
    val fecha :String
)