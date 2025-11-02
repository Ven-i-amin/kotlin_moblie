package ru.vsu.task1.composables.trade

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.VicoZoomState
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider.Companion.verticalGradient
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.DashedShape
import ru.vsu.task1.u1.theme.defaultScheme
import kotlin.random.Random
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer as LineLayer

@Composable
fun CurrencyChart(costs: List<Float>, modifier: Modifier = Modifier) {
    val lastPrice by remember { mutableFloatStateOf(costs.last()) }
    val minPrice by remember { mutableFloatStateOf(costs.min()) }
    val maxPrice by remember { mutableFloatStateOf(costs.max()) }
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(costs) {
        val chartPoints = costs.mapIndexed {
            index, cost -> index to cost
        }.toMap()
        modelProducer.runTransaction {
            lineSeries { series(chartPoints.keys, chartPoints.values) }
        }
    }

    Box(modifier = modifier) {
        ChartHost(
            modelProducer = modelProducer,
            priceLine =priceLine(lastPrice),
            yLayerRange = layerRange(
                minY = minPrice.toDouble(),
                maxY = maxPrice.toDouble()
            )
        )
    }
}


@Composable
fun priceLine(price: Float): HorizontalLine {
    val line = rememberLineComponent(
        fill = fill(
            color = defaultScheme.onSecondary
        ),
        shape = DashedShape()
    )

    val label = rememberTextComponent(
        color = defaultScheme.onSecondary,
        textSize = TextUnit(12f, TextUnitType.Sp),
        padding = insets(4.dp),
        background = shapeComponent(
            fill = fill(
                color = defaultScheme.secondary
            ),
            CorneredShape.rounded(8.dp)
        )
    )


    return remember {
        HorizontalLine(
            y = { price.toDouble() },
            line = line,
            labelComponent = label,
            label = { "$${price}" },
            verticalLabelPosition = Position.Vertical.Center
        )
    }
}

@Composable
fun layerRange(
    minX: Double? = null,
    minY: Double? = null,
    maxX: Double? = null,
    maxY: Double? = null
) : CartesianLayerRangeProvider {
    return CartesianLayerRangeProvider.fixed(
        minX = minX,
        minY = minY,
        maxX = maxX,
        maxY = maxY
    )
}

@Composable
fun ChartHost(
    modelProducer: CartesianChartModelProducer = remember { CartesianChartModelProducer() },
    priceLine: HorizontalLine,
    yLayerRange: CartesianLayerRangeProvider
) {
    val lineLayer = rememberLineCartesianLayer(
        lineProvider = LineLayer.LineProvider.series(
            listOf(
                LineLayer.Line(
                    fill = LineLayer.LineFill.single(
                        fill(
                            color = defaultScheme.primary
                        )
                    ),
                    stroke = LineLayer.LineStroke.Continuous(),
                    areaFill = LineLayer.AreaFill.single(
                        fill = fill(
                            shaderProvider = verticalGradient(
                                colors = intArrayOf(
                                    defaultScheme.primary.toArgb(),
                                    Color.Transparent.toArgb()
                                )
                            )
                        ),
                    ),
                    pointConnector = LineLayer.PointConnector.cubic(),
                ),
            )
        ),
        rangeProvider = yLayerRange
    )

    CartesianChartHost(
        chart = rememberCartesianChart(
            lineLayer,
            decorations = listOf(
                priceLine
            ),
        ),
        modelProducer = modelProducer,
        modifier = Modifier.fillMaxHeight(),
    )
}

@Preview
@Composable
fun PreviewLineChart() {
    var clicked by remember { mutableIntStateOf(0) }

    val values = remember(clicked) {
        val random = Random(clicked)
        List(10) { random.nextFloat() * 100f + 1000 }
    }

    MaterialTheme {
        Column {
            CurrencyChart(costs = values)
            Button(onClick = { clicked++ }) {
                Text("Clicked $clicked times")
            }
        }
    }
}
