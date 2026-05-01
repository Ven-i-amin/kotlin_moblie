package ru.vsu.task1.utils.pointposition

import androidx.compose.ui.geometry.Offset

interface PointPositionSpec {
    fun isSatisfiedBy(value: Offset): Boolean
}