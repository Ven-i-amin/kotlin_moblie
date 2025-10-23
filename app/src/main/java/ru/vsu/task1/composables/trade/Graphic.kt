package ru.vsu.task1.composables.trade

import android.graphics.drawable.shapes.RectShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatryk.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatryk.vico.compose.style.ProvideChartStyle
import com.patrykandpatryk.vico.core.DefaultAlpha
import com.patrykandpatryk.vico.core.chart.line.LineChart
import com.patrykandpatryk.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry

import ru.vsu.task1.u1.theme.defaultScheme

@Composable
fun Graphic(costs: List<Float>) {
    val modelProducer = remember { ChartEntryModelProducer() }
    val datasetLineSpec = remember {
        arrayListOf(
            LineChart.LineSpec(
                lineColor = defaultScheme.primary.toArgb(),
                lineBackgroundShader = DynamicShaders.fromBrush(
                    brush = Brush.verticalGradient(
                        listOf(
                            defaultScheme.primary.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                            defaultScheme.primary.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                        )
                    )
                )
            )
        )
    }

    LaunchedEffect(key1 = costs) {
        var xPos = 0f
        val dataPoints = arrayListOf<FloatEntry>()

        for (cost in costs) {
            dataPoints.add(FloatEntry(xPos, cost))
            xPos++
        }

        modelProducer.setEntries(dataPoints)
    }

    ProvideChartStyle {
        Chart(
            chart = lineChart(
                lines = datasetLineSpec,
            ),
            chartModelProducer = modelProducer,
            isZoomEnabled = true
        )

    }
}

@Preview
@Composable
fun PreviewLineChart() {
    MaterialTheme {
        Graphic(listOf(1f, 2f, 3f, 1f, 8f))
    }
}