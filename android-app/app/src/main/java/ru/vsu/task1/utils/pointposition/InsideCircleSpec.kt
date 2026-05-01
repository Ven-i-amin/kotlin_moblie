package ru.vsu.task1.utils.pointposition

import androidx.compose.ui.geometry.Offset

class InsideCircleSpec(
    private val center: Offset,
    private val radius: Float
) : PointPositionSpec {
    override fun isSatisfiedBy(value: Offset): Boolean = (value - center).getDistance() <= radius
}