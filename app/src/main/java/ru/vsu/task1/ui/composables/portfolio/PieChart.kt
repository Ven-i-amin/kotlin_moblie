package ru.vsu.task1.ui.composables.portfolio

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.vsu.task1.ui.theme.AppColors.DarkGreyGradient
import ru.vsu.task1.ui.theme.AppColors.LightBlueGradient
import ru.vsu.task1.ui.theme.AppColors.PurpleGradient
import ru.vsu.task1.ui.theme.AppColors.PurplePinkGradient
import ru.vsu.task1.ui.theme.defaultScheme
import ru.vsu.task1.utils.pointposition.InsideCircleSpec
import ru.vsu.task1.utils.pointposition.InsideSectorSpec
import ru.vsu.task1.utils.pointposition.PointPositionCompareSpec
import kotlin.math.cos
import kotlin.math.sin

private val gradients = listOf(
    PurplePinkGradient,
    PurpleGradient,
    LightBlueGradient,
)

private const val INNER_CLICKABLE_BORDER_RATIO = 2

@Composable
fun PieChart(
    data: List<Pair<String, Double>>,
    pieRadius: Dp = 140.dp,
    pieLineWidth: Dp = 35.dp,
    sectorsGapAngle: Float = 2f,
    animDuration: Int = 1000,
    onSectorClick: (Int) -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    val totalSum = data.sumOf { it.second }

    val sectorsAngles = mutableListOf<Pair<Float, Float>>()
    var selectedIndex by remember { mutableIntStateOf(-1) }

//    var selectedCircleX by remember {mutableFloatStateOf(0f)}
//    var selectedCircleY by remember {mutableFloatStateOf(0f)}
    val overlapAnim = remember(selectedIndex) { Animatable(0.5f) }

    LaunchedEffect(overlapAnim, animDuration) {
        if (selectedIndex != -1) {
            overlapAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = animDuration)
            )
        }
    }

    data.map{ it.second }.forEach { values ->
        val lastStartAngle = sectorsAngles.lastOrNull()?.first ?: 0f
        val lastSweepAngle = sectorsAngles.lastOrNull()?.second ?: 0f

        val startAngle = lastStartAngle + lastSweepAngle + sectorsGapAngle
        val sweepAngle = 360f * values.toFloat() / totalSum.toFloat() - sectorsGapAngle

        sectorsAngles.add(startAngle to sweepAngle)
    }

    val pieRadiusFloat = with(LocalDensity.current) { pieRadius.toPx() }
    val innerRadiusPx = pieRadiusFloat / INNER_CLICKABLE_BORDER_RATIO
    val pieCenter = Offset(pieRadiusFloat, pieRadiusFloat)

    val pointPositionCheck = PointPositionCompareSpec(
        InsideCircleSpec(
            center = pieCenter,
            radius = pieRadiusFloat
        )
    ).andNot(
        InsideCircleSpec(
            center = pieCenter,
            radius = innerRadiusPx
        )
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .background(Color.Transparent)
                    .offset { IntOffset.Zero }
                    .size(pieRadius * 2f)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
//                            selectedCircleX = offset.x
//                            selectedCircleY = offset.y
                            if (pointPositionCheck.isSatisfiedBy(offset)) {
                                selectedIndex = findSectorInPointIndex(
                                    offset = offset,
                                    sectorsAngles = sectorsAngles,
                                    pieRadius = pieRadiusFloat + 20
                                )

                                onSectorClick(selectedIndex)
                            }
                        }
                    }
            ) {
                drawOverlapCircle(
                    pieWidth = pieLineWidth.toPx(),
                    backgroundColor = colors.secondary,
                    overlapCoefficient = 0f
                )

                sectorsAngles.forEachIndexed { index, value ->
                    if (index != selectedIndex) {
                        drawSector(
                            pieRadius = pieRadiusFloat,
                            pieCenter = pieCenter,
                            pieLineWidth = pieLineWidth.toPx(),
                            gradient = gradients[index % gradients.size],
                            value = value
                        )
                    }
                }

                drawOverlapCircle(
                    pieWidth = pieLineWidth.toPx(),
                    backgroundColor = colors.secondary,
                    overlapCoefficient = 0.5f
                )

                if (selectedIndex != -1) {
                    drawSector(
                        pieRadius = pieRadiusFloat,
                        pieCenter = pieCenter,
                        pieLineWidth = pieLineWidth.toPx(),
                        gradient = gradients[selectedIndex % gradients.size],
                        value = sectorsAngles[selectedIndex]
                    )
                }

                drawOverlapCircle(
                    pieWidth = pieLineWidth.toPx(),
                    backgroundColor = colors.secondary,
                    overlapCoefficient = overlapAnim.value
                )

                drawOverlapCircle(
                    pieWidth = pieLineWidth.toPx(),
                    backgroundColor = colors.background,
                    overlapCoefficient = 1f
                )
            }
        }

//        Text(
//            text = "${selectedIndex}  x=${selectedCircleX}  y=${selectedCircleY}"
//        )
    }
}

fun findSectorInPointIndex(
    offset: Offset,
    sectorsAngles: List<Pair<Float, Float>>,
    pieRadius: Float
): Int {
    for (index in sectorsAngles.indices) {
        if (InsideSectorSpec(
                center = Offset(pieRadius, pieRadius),
                radius = pieRadius,
                startAngle = sectorsAngles[index].first,
                sweepAngle = sectorsAngles[index].second
            ).isSatisfiedBy(offset)) {
            return index
        }
    }

    return -1
}

private fun DrawScope.drawSector(
    pieRadius: Float,
    pieCenter: Offset,
    pieLineWidth: Float,
    gradient: List<Color>,
    value: Pair<Float, Float>,
) {
    if (!value.first.isFinite() || !value.second.isFinite() || value.second <= 0f) {
        return
    }

    val endAngle = value.first + value.second

    val startOffset = Offset(
        x = pieCenter.x + pieRadius * cos(Math.toRadians(value.first.toDouble())).toFloat(),
        y = pieCenter.y + pieRadius * sin(Math.toRadians(value.first.toDouble())).toFloat()
    )
    val endOffset = Offset(
        x = pieCenter.x + pieRadius * cos(Math.toRadians(endAngle.toDouble())).toFloat(),
        y = pieCenter.y + pieRadius * sin(Math.toRadians(endAngle.toDouble())).toFloat()
    )

    drawArc(
        brush = Brush.linearGradient(
            colors = gradient,
            start = startOffset,
            end = endOffset
        ),
        topLeft = Offset(
            x = pieLineWidth / 2,
            y = pieLineWidth / 2
        ),
        size = Size(
            width =  size.width - pieLineWidth,
            height = size.height - pieLineWidth
        ),
        startAngle = value.first,
        sweepAngle = value.second,
        useCenter = false,
        style = Stroke(
            width = pieLineWidth,
            cap = StrokeCap.Butt,
        )
    )
}

private fun DrawScope.drawOverlapCircle(
    pieWidth: Float,
    backgroundColor: Color,
    overlapCoefficient: Float
) {
    drawCircle(
        color = backgroundColor,
        radius = size.minDimension / 2.0f - pieWidth * overlapCoefficient,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    MaterialTheme(colorScheme = defaultScheme) {
        PieChart(
            data = listOf(
                "Solana" to 100.0,
                "Bitcoin" to 200.0,
                "Ethereum" to 300.0,
                "Dogecoin" to 400.0
            ),
            pieLineWidth = 30.dp
        )
    }
}
