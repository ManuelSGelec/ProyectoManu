import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.manuel.fintrack.ui.theme.FintrackTheme


@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
        Text(
            text = "Bienvenido Manu",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Contenedor de Cards con LazyVerticalGrid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),

            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Cuatro tarjetas en grid 2x2
            item {
                FinanceCard(
                    title = "Balances",
                    icon = Icons.Filled.TrendingUp,
                    color = Color(0xFFFD6A6A),
                    navController = navController,
                    route = "balance"

                )
            }
            item {
                FinanceCard(
                    title = "Notificaciones",
                    icon = Icons.Filled.Email,
                    color = Color(0xFF4DCBC2),
                    navController = navController,
                    route = "Messages"
                )
            }
            item {
                FinanceCard(
                    title = "Presupuestos",
                    icon = Icons.Filled.ListAlt,
                    color = Color(0xFF44B6CF),
                    navController = navController,
                    route = "Budgets"
                )
            }
            item {
                FinanceCard(
                    title = "Cuentas compartidas",
                    icon = Icons.Filled.People,
                    color = Color(0xFF95CCB3),
                    navController = navController,
                    route = "SaharedAccounts"
                )
            }
            item {
                FinanceCard(
                    title = "Asistencia Financiera",
                    icon = Icons.Filled.HeadsetMic,
                    color = Color(0xFFFFB74D),
                    navController = navController,
                    route = "Attendance"
                )
            }


        }

        Spacer(modifier = Modifier.height(24.dp))


    }
}

@Composable
fun FinanceCard(
    title: String,
    icon: ImageVector,
    color: Color,
    route: String = "",
    navController: NavController = rememberNavController(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { navController.navigate(route) },

        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    FintrackTheme {
        MainScreen(rememberNavController())
    }
}