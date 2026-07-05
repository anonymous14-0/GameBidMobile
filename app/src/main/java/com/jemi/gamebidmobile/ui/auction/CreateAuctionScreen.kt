/*
 * File: CreateAuctionScreen.kt
 * Fungsi: Layer UI Jetpack Compose. File ini membangun tampilan, membaca state dari ViewModel, dan mengirim event pengguna ke alur UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.ui.auction

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel
import com.jemi.gamebidmobile.ui.components.EmptyState
import com.jemi.gamebidmobile.ui.components.ErrorState
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
// Composable ini membangun bagian UI CreateAuctionScreen.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
fun CreateAuctionScreen(
    viewModel: AuctionViewModel = viewModel()
) {
    val context = LocalContext.current

    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()

    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    var expanded by remember { mutableStateOf(false) }
    var selectedItemId by remember { mutableIntStateOf(0) }
    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    var selectedItemTitle by remember { mutableStateOf("") }
    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    var startTime by remember { mutableStateOf("") }
    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    var endTime by remember { mutableStateOf("") }
    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    var localMessage by remember { mutableStateOf("") }
    // State UI: SnackbarHostState menampung antrean pesan validasi/sukses dari ViewModel agar feedback tampil konsisten.
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (token != null) {
            viewModel.loadSellerItems(token)
        }
    }

    LaunchedEffect(localMessage, viewModel.createMessage) {
        val message = localMessage.ifEmpty { viewModel.createMessage }
        if (message.isNotEmpty()) snackbarHostState.showSnackbar(message)
        if (viewModel.createMessage == "Auction berhasil dibuat") {
            selectedItemId = 0
            selectedItemTitle = ""
            startTime = ""
            endTime = ""
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Create Auction",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    OutlinedTextField(
                        value = selectedItemTitle,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = {
                            Text("Pilih Item")
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        viewModel.sellerItems.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(item.title)
                                },
                                onClick = {
                                    selectedItemId = item.id
                                    selectedItemTitle = item.title
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (viewModel.sellerItems.isEmpty() && viewModel.loadErrorMessage.isEmpty()) {
                    EmptyState(
                        modifier = Modifier.height(280.dp),
                        icon = Icons.Outlined.Inventory2,
                        title = "Belum ada item",
                        subtitle = "Tambahkan item terlebih dahulu sebelum membuat auction baru.",
                        actionLabel = null,
                        onActionClick = null
                    )
                }

                if (viewModel.loadErrorMessage.isNotEmpty()) {
                    ErrorState(
                        modifier = Modifier.height(280.dp),
                        title = "Item gagal dimuat",
                        subtitle = "Daftar item seller belum bisa ditampilkan. Coba muat ulang.",
                        onActionClick = { token?.let { viewModel.loadSellerItems(it) } }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        pickDateTime(context) {
                            startTime = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (startTime.isEmpty())
                            "Pilih Start Time"
                        else
                            startTime
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        pickDateTime(context) {
                            endTime = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (endTime.isEmpty())
                            "Pilih End Time"
                        else
                            endTime
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = !viewModel.isLoading,
                    onClick = {
                        localMessage = ""

                        if (token == null) {
                            localMessage = "Token tidak ditemukan"
                        } else if (selectedItemId == 0) {
                            localMessage = "Pilih item dulu"
                        } else if (startTime.isEmpty()) {
                            localMessage = "Pilih start time"
                        } else if (endTime.isEmpty()) {
                            localMessage = "Pilih end time"
                        } else {
                            viewModel.createAuction(
                                token,
                                selectedItemId,
                                startTime,
                                endTime
                            )
                        }
                    }
                ) {
                    com.jemi.gamebidmobile.ui.components.LoadingButtonContent("Buat Auction", viewModel.isLoading)
                }
            }
        }

    }
    }
}

fun pickDateTime(
    context: android.content.Context,
    onDateTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, day ->

            TimePickerDialog(
                context,
                { _, hour, minute ->

                    val dateTime = String.format(
                        "%04d-%02d-%02d %02d:%02d:00",
                        year,
                        month + 1,
                        day,
                        hour,
                        minute
                    )

                    onDateTimeSelected(dateTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}
