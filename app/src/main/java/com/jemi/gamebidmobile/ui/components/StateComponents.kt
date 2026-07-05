package com.jemi.gamebidmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Inventory2,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    ModernStateCard(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = subtitle,
        actionLabel = actionLabel,
        onActionClick = onActionClick,
        isError = false
    )
}

@Composable
fun ErrorState(
    title: String = "Data gagal dimuat",
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.CloudOff,
    actionLabel: String? = "Coba lagi",
    onActionClick: (() -> Unit)? = null
) {
    ModernStateCard(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = subtitle,
        actionLabel = actionLabel,
        onActionClick = onActionClick,
        isError = true
    )
}

@Composable
private fun ModernStateCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier,
    actionLabel: String?,
    onActionClick: (() -> Unit)?,
    isError: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 34.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(78.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = if (isError) {
                                    listOf(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.secondaryContainer)
                                } else {
                                    listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer)
                                }
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                if (actionLabel != null && onActionClick != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    if (isError) {
                        OutlinedButton(
                            onClick = onActionClick,
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(actionLabel)
                        }
                    } else {
                        Button(
                            onClick = onActionClick,
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(actionLabel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmActionDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDestructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = if (isDestructive) {
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun LoadingButtonContent(text: String, isLoading: Boolean) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    } else {
        Text(text)
    }
}
