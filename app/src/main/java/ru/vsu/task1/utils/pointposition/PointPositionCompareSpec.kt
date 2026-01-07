package ru.vsu.task1.utils.pointposition

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.Paragraph

class PointPositionCompareSpec(val spec: PointPositionSpec): PointPositionSpec {
    private val specs: MutableList<Pair<PointPositionSpec, Int>> = mutableListOf()

    fun and(spec: PointPositionSpec): PointPositionCompareSpec {
        specs.add(spec to 1)
        return this
    }

    fun or(spec: PointPositionSpec): PointPositionCompareSpec {
        specs.add(spec to 2)
        return this
    }

    fun andNot(spec: PointPositionSpec): PointPositionCompareSpec {
        specs.add(spec to 3)
        return this
    }

    override fun isSatisfiedBy(value: Offset): Boolean {
        var result = spec.isSatisfiedBy(value)

        specs.forEach { spec ->
            when (spec.second) {
                1 -> {
                    result = spec.first.isSatisfiedBy(value)

                    if (!result) {
                        return false
                    }
                }

                2 -> {
                    result = spec.first.isSatisfiedBy(value)

                    if (result) {
                        return true
                    }
                }

                3 -> {
                    result = !spec.first.isSatisfiedBy(value)

                    if (!result) {
                        return false
                    }
                }

                4 -> {
                    result = !spec.first.isSatisfiedBy(value)

                    if (result) {
                        return true
                    }
                }
            }
        }

        return result
    }
}