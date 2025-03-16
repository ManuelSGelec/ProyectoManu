package com.manuel.fintrack.componentes

import android.graphics.Color.BLACK
import android.graphics.Color.BLUE
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


@Composable
fun ComposePieChart(
    modifier: Modifier = Modifier,
    entries: List<PieEntry>,
    colors: List<Color>,
    description: String = ""
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                this.description.text = description
                val dataSet = PieDataSet(entries, "").apply {
                    // Convierte la lista de Color a IntArray
                    this.colors = colors.map { it.toArgb() }
                }
                this.data = PieData(dataSet)
                invalidate()
            }
        },
        update = { chart ->
            val dataSet = PieDataSet(entries, "").apply {
                // Convierte la lista de Color a IntArray
                this.colors = colors.map { it.toArgb() }
            }
            chart.data = PieData(dataSet)
            chart.description.text = description
            chart.invalidate()
        }
    )
}


@Composable
fun BarChartByCategory(data: Map<String, Double>, title: String) {
    val context = LocalContext.current
    val barChart = remember { BarChart(context) }

    LaunchedEffect(data) {
        val entries = mutableListOf<BarEntry>()
        val labels = data.keys.toList()
        data.values.forEachIndexed { index, amount ->
            entries.add(BarEntry(index.toFloat(), amount.toFloat()))
        }

        val dataSet = BarDataSet(entries, title)
        dataSet.color = BLUE
        dataSet.valueTextColor = BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        barChart.data = barData

        // Personalización del eje X
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -45f

        // Personalización del eje Y (izquierdo)
        val yAxisLeft = barChart.axisLeft
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.axisMinimum = 0f

        // Deshabilitar el eje Y derecho y la leyenda
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false

        barChart.animateY(1000)
        barChart.invalidate()
    }

    AndroidView(

        factory = {

            barChart
        },
        update = { chart ->
            // Los datos se actualizan en el LaunchedEffect
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    )
}
