/*
 * File: TransactionScreen.kt
 * Fungsi: Layer UI Jetpack Compose. File ini membangun tampilan, membaca state dari ViewModel, dan mengirim event pengguna ke alur UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.data.model.TransactionModel
import com.jemi.gamebidmobile.ui.components.TransactionStatusBadge
import com.jemi.gamebidmobile.ui.components.EmptyState
import com.jemi.gamebidmobile.ui.components.ErrorState
import com.jemi.gamebidmobile.ui.components.formatRupiah
import com.jemi.gamebidmobile.viewmodel.TransactionViewModel


// Composable ini membangun bagian UI TransactionScreen.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
fun TransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val context = LocalContext.current
    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    val tokenManager = remember { TokenManager(context) }
    val token = tokenManager.getToken()

    LaunchedEffect(token) {
        token?.let { viewModel.loadTransactions(it) }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            viewModel.isLoading -> TransactionLoadingState()
            viewModel.loadErrorMessage.isNotEmpty() -> ErrorState(
                subtitle = "Transaksi gagal dimuat. Periksa koneksi internet atau coba beberapa saat lagi.",
                onActionClick = { token?.let { viewModel.loadTransactions(it) } }
            )
            viewModel.transactions.isEmpty() -> EmptyState(
                icon = Icons.Outlined.ReceiptLong,
                title = "Belum ada transaksi",
                subtitle = "Transaksi penjualan dari auction yang berhasil akan tampil di sini.",
                actionLabel = "Refresh",
                onActionClick = { token?.let { viewModel.loadTransactions(it) } }
            )
            else -> TransactionList(
                transactions = viewModel.transactions,
                onTransactionClick = { transactionId ->
                    navController.navigate("transaction_detail/$transactionId")
                }
            )
        }
    }
}

// Composable ini membangun bagian UI TransactionList.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
private fun TransactionList(
    transactions: List<TransactionModel>,
    onTransactionClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 24.dp,
            end = 20.dp,
            bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TransactionHeader(totalTransactions = transactions.size)
        }

        items(
            items = transactions,
            key = { transaction -> transaction.id }
        ) { transaction ->
            TransactionCard(
                transaction = transaction,
                onClick = { onTransactionClick(transaction.id) }
            )
        }
    }
}

// Composable ini membangun bagian UI TransactionHeader.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
private fun TransactionHeader(totalTransactions: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Penjualan Seller",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "$totalTransactions transaksi penjualan terbaru",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Composable ini membangun bagian UI TransactionCard.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
private fun TransactionCard(
    transaction: TransactionModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Transaction ID",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "#${transaction.id}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                TransactionStatusBadge(status = transaction.status)
            }

            AuctionInfoSection(transaction = transaction)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Amount",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatRupiah(transaction.amount),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Text(
                    text = "Lihat detail",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Composable ini membangun bagian UI AuctionInfoSection.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
private fun AuctionInfoSection(transaction: TransactionModel) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Storefront,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Auction ID / Item",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = transaction.itemTitleOrAuctionId(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Composable ini membangun bagian UI TransactionLoadingState.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
private fun TransactionLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                Text(
                    text = "Memuat transaksi...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Mohon tunggu sebentar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun TransactionModel.itemTitleOrAuctionId(): String {
    val itemTitle = auction?.item?.title
    return if (itemTitle.isNullOrBlank()) {
        "Auction #$auction_id"
    } else {
        itemTitle
    }
}
