package com.jemi.gamebidmobile.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            "active" -> Color(0xFF4CAF50)
            "ended" -> Color(0xFFF44336)
            else -> Color.Gray
        }

    Surface(
        color = color,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = status.uppercase(),
            color = Color.White,
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
        "pending_payment" -> Color(0xFFFF9800)
        "verified",
        "payment_verified" -> Color(0xFF2196F3)
        "account_sent" -> Color(0xFF9C27B0)
        "completed" -> Color(0xFF4CAF50)
        else -> Color(0xFF6B7280)
    }

    Surface(
        color = color,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = status.replace("_", " ").uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 7.dp
            )
        )
    }
}