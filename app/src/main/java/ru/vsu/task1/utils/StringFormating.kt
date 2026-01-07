package ru.vsu.task1.utils

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat
import java.util.Locale
import kotlin.math.abs

fun formatPrice(price: Double?): String = price?.let { value ->
    "$" + formatDecimal(value)
} ?: "Not available"

fun formatPercentage(percentage: Double?): String {
    val percentageChange = percentage ?: 0.0

    val sign = if (percentageChange >= 0) "+" else "-"
    return sign + formatDecimal(abs(percentageChange)) + "%"
}

fun formatTimestamp(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern(
        "dd MMM yyyy, h.mm a",
        Locale.ENGLISH
    )

    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun formatSignificantDigit(value: Double): DecimalFormat {
    if (value >= 1) {
        return DecimalFormat("0.00")
    }

    val fraction = value
        .toBigDecimal()
        .toString()
        .substringAfter(
            '.',
            ""
        )

    for (zeroCount in 0..fraction.length) {
        if (fraction[zeroCount] != '0') {
            return DecimalFormat("0.00${"0".repeat(zeroCount)}")
        }
    }

    return DecimalFormat("#.#")
}

fun formatDecimal(value: Double): String {
    return formatSignificantDigit(value).format(value)
}
