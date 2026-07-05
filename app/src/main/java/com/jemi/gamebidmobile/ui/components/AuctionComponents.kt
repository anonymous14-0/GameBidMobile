package com.jemi.gamebidmobile.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun calculateRemainingTime(endTime: String): String {
    return try {
        val cleanTime = endTime
            .replace("T", " ")
            .substringBefore(".")

        val format = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        )

        val endDate =
            format.parse(cleanTime)
                ?: return "Timer Error"

        val diff =
            endDate.time - System.currentTimeMillis()

        if (diff <= 0) {
            "Auction Ended"
        } else {
            val days = diff / (1000 * 60 * 60 * 24)
            val hours = (diff / (1000 * 60 * 60)) % 24
            val minutes = (diff / (1000 * 60)) % 60
            val seconds = (diff / 1000) % 60

            "${days}h ${hours}j ${minutes}m ${seconds}d"
        }
    } catch (e: Exception) {
        "Timer Error"
    }
}

fun formatRupiah(value: Number): String {
    val formatter =
        NumberFormat.getCurrencyInstance(
            Locale("id", "ID")
        )

    formatter.maximumFractionDigits = 0

    return formatter.format(value)
}

@Composable
fun StatusBadge(status: String) {
    val color =
        when (status.lowercase()) {
            "active" -> MaterialTheme.colorScheme.tertiary
            "ended" -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.surfaceVariant
        }

    val contentColor = if (status.lowercase() !in listOf("active", "ended")) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    Surface(
        color = color,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = status.uppercase(),
            color = contentColor,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
        )
    }
}

@Composable
fun TransactionStatusBadge(status: String) {
    val color = when (status.lowercase()) {
        "pending",
        "pending_payment" -> MaterialTheme.colorScheme.secondary
        "verified",
        "payment_verified" -> MaterialTheme.colorScheme.primary
        "account_sent" -> MaterialTheme.colorScheme.primaryContainer
        "completed" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (status.lowercase()) {
        "account_sent" -> MaterialTheme.colorScheme.onPrimaryContainer
        "pending",
        "pending_payment",
        "verified",
        "payment_verified",
        "completed" -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = color,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = status.replace("_", " ").uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 7.dp
            )
        )
    }
}