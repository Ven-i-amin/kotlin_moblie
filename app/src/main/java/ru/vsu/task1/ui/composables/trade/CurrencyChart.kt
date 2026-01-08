package ru.vsu.task1.ui.composables.trade

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider.Companion.verticalGradient
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.DashedShape
import com.patrykandpatrick.vico.core.common.shape.Shape
import ru.vsu.task1.ui.screens.trade.TradeViewModel
import ru.vsu.task1.ui.composables.generic.Loading
import ru.vsu.task1.utils.formatDecimal
import ru.vsu.task1.utils.formatSignificantDigit
import java.text.DecimalFormat
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer as LineLayer

const val STEPS = 50
const val CIRCLE_ANGLE = 360f
const val DELTA_STEP = CIRCLE_ANGLE / STEPS
const val GRADUS = PI / 180


private val circle: Shape = Shape { _, path, left, top, right, bottom ->
    val xRadius = abs(left - right) / 2f
    val yRadius = abs(top - bottom) / 2f
    val xCenter = (left + right) / 2f
    val yCenter = (top + bottom) / 2f

    path.moveTo(xCenter + xRadius, yCenter)

    for (i in 0..STEPS) {
        val angle = i * DELTA_STEP * GRADUS
        path.lineTo(
            ((xCenter + xRadius * cos(angle)).toFloat()),
            ((yCenter + yRadius * sin(angle)).toFloat())
        )
    }
    path.close()
}

private const val GRAPHIC_PADDING = 0f

@Composable
fun CurrencyChart(
    modifier: Modifier = Modifier,
    prices: List<Float>,
    shadow: Boolean = true,
    marker: Boolean = true,
    priceLine: Boolean = true
) {
    val lastPrice = prices.last()
    val minPrice = prices.min() - GRAPHIC_PADDING
    val maxPrice = prices.max() + GRAPHIC_PADDING
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(prices) {
        modelProducer.runTransaction {
            lineSeries { series(prices) }
        }
    }

    Box(modifier = modifier) {
        ChartHost(
            modelProducer = modelProducer,
            priceLine = if (priceLine) priceLine(lastPrice) else null,
            layerRange = layerRange(
                minY = minPrice.toDouble(),
                maxY = maxPrice.toDouble()
            ),
            valueFormatter = formatSignificantDigit(prices.last().toDouble()),
            shadow = shadow,
            marker = marker,
        )
    }
}


@Composable
private fun priceLine(price: Float): HorizontalLine {
    val colors = MaterialTheme.colorScheme

    val line = rememberLineComponent(
        fill = fill(
            color = colors.onSecondary
        ),
        shape = DashedShape()
    )

    val label = rememberTextComponent(
        color = colors.onSecondary,
        textSize = TextUnit(12f, TextUnitType.Sp),
        padding = insets(4.dp),
        background = shapeComponent(
            fill = fill(
                color = colors.secondary
            ),
            CorneredShape.rounded(8.dp)
        )
    )


    return HorizontalLine(
        y = { price.toDouble() },
        line = line,
        labelComponent = label,
        label = { "$${formatDecimal(price.toDouble())}" },
        verticalLabelPosition = Position.Vertical.Center
    )
}

@Composable
private fun layerRange(
    minX: Double? = null,
    minY: Double? = null,
    maxX: Double? = null,
    maxY: Double? = null
): CartesianLayerRangeProvider {
    return CartesianLayerRangeProvider.fixed(
        minX = minX,
        minY = minY,
        maxX = maxX,
        maxY = maxY
    )
}

@Composable
private fun ChartHost(
    modelProducer: CartesianChartModelProducer = remember { CartesianChartModelProducer() },
    priceLine: HorizontalLine?,
    layerRange: CartesianLayerRangeProvider,
    valueFormatter: DecimalFormat,
    shadow: Boolean,
    marker: Boolean,
) {
    val colors = MaterialTheme.colorScheme

    val areaFill = if (shadow) {
        fill(
            shaderProvider = verticalGradient(
                colors = intArrayOf(
                    colors.primary.toArgb(),
                    Color.Transparent.toArgb()
                )
            )
        )
    } else {
        Fill.Transparent
    }

    val lineLayer = rememberLineCartesianLayer(
        lineProvider = LineLayer.LineProvider.series(
            listOf(
                LineLayer.Line(
                    fill = LineLayer.LineFill.single(
                        fill(
                            color = colors.primary
                        )
                    ),
                    stroke = LineLayer.LineStroke.Continuous(),
                    areaFill = LineLayer.AreaFill.single(
                        fill = areaFill,
                    ),
                    pointConnector = LineLayer.PointConnector.cubic(),
                ),
            )
        ),
        rangeProvider = layerRange
    )

    val marker = if (marker) rememberDefaultCartesianMarker(
        label = TextComponent(),
        valueFormatter = DefaultCartesianMarker.ValueFormatter.default(valueFormatter),
        indicator = { color ->
            ShapeComponent(
                fill = fill(color),
                shape = circle,
                strokeFill = fill(colors.onPrimary),
                strokeThicknessDp = 3f
            )
        }
    ) else null

    CartesianChartHost(
        chart = rememberCartesianChart(
            lineLayer,
            decorations = listOfNotNull(priceLine),
            marker = marker,
            getXStep = { it.width }
        ),
        modelProducer = modelProducer,
        modifier = Modifier.fillMaxHeight(),
        placeholder = { Loading(modifier = Modifier.fillMaxSize()) }
    )
}

@Preview
@Composable
private fun PreviewLineChart() {
    val viewModel: TradeViewModel = viewModel()
    val prices by viewModel.prices.collectAsState()
    var clicked by remember { mutableIntStateOf(0) }

    val values = remember(clicked) {
        val random = Random(clicked)
        List(10) { random.nextFloat() * 100f + 1000 }
    }

    print(prices)

    MaterialTheme {
        Column {
            CurrencyChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                prices = prices.ifEmpty { values }
            )
            Button(onClick = { viewModel.fetchMarketChart("bitcoin", timeGaps = "1") }) {
                Text("Clicked $clicked times")
            }
        }
    }
}
