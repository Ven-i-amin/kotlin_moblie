package ru.vsu.task1.utils.pointposition

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2

class InsideSectorSpec(
    private val center: Offset,
    private val radius: Float,
    private val startAngle: Float,
    private val sweepAngle: Float
) : PointPositionSpec {
    override fun isSatisfiedBy(value: Offset): Boolean {
        val vector = value - center
        val distance = vector.getDistance()
        val angle = ((Math.toDegrees(atan2(vector.y, vector.x).toDouble()) + 360.0) % 360.0).toFloat()
        val start = ((startAngle % 360f) + 360f) % 360f
        val sweep = sweepAngle.coerceIn(0f, 360f)
        val end = (start + sweep) % 360f
        val inAngle = if (sweep >= 360f) {
            true
        } else if (start <= end) {
            angle in start..end
        } else {
            angle >= start || angle <= end
        }

        return distance <= radius && inAngle
    }
}
