package com.jemi.gamebidmobile.ui.transaction

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.utils.uriToFile
import com.jemi.gamebidmobile.viewmodel.TransactionViewModel
import com.jemi.gamebidmobile.ui.components.TransactionStatusBadge
import com.jemi.gamebidmobile.ui.components.formatRupiah
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@Composable
fun TransactionDetailScreen(
    transactionId: Int,
    viewModel: TransactionViewModel = viewModel()
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    val token = tokenManager.getToken()
    val role = tokenManager.getRole()

    LaunchedEffect(Unit) {
        if (token != null) {
            viewModel.loadTransactionDetail(token, transactionId)
        }
    }

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var accountEmail by remember { mutableStateOf("") }
    var accountPassword by remember { mutableStateOf("") }
    var sellerNote by remember { mutableStateOf("") }

    val transaction = viewModel.selectedTransaction
    val transactionStatus = transaction?.status ?: "loading"

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            selectedUri = uri
        }

    if (transaction == null) {
        Box(Modifier.fillMaxSize()) {
            Text("Loading...")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // HEADER CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Transaksi #$transactionId",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = formatRupiah(transaction.amount),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                TransactionStatusBadge(transactionStatus)
            }
        }

        when (transactionStatus) {

            "pending_payment" -> {
                if (role == "pembeli") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                "Instruksi Pembayaran",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Payment")
                            Text("DANA")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Nomor DANA")
                            Text(
                                "081215692885",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Atas Nama")
                            Text("Jemi Gaming")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            launcher.launch("image/*")
                        }
                    ) {
                        Text("Pilih Bukti Transfer")
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (selectedUri != null && token != null) {

                                val file = uriToFile(
                                    context,
                                    selectedUri!!
                                )

                                val requestFile =
                                    file.asRequestBody(
                                        "image/*".toMediaTypeOrNull()
                                    )

                                val imagePart =
                                    MultipartBody.Part.createFormData(
                                        "payment_proof",
                                        file.name,
                                        requestFile
                                    )

                                viewModel.uploadProof(
                                    token,
                                    transactionId,
                                    imagePart
                                )
                            }
                        }
                    ) {
                        Text("Upload Bukti Transfer")
                    }

                } else {
                    Text("Menunggu pembeli upload bukti transfer")
                }
            }

            "payment_verified", "escrow" -> {
                if (role == "penjual") {

                    OutlinedTextField(
                        value = accountEmail,
                        onValueChange = { accountEmail = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email Akun") }
                    )

                    OutlinedTextField(
                        value = accountPassword,
                        onValueChange = { accountPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Password Akun") }
                    )

                    OutlinedTextField(
                        value = sellerNote,
                        onValueChange = { sellerNote = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Catatan Seller") }
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (
                                token != null &&
                                accountEmail.isNotEmpty() &&
                                accountPassword.isNotEmpty()
                            ) {
                                viewModel.sendAccount(
                                    token,
                                    transactionId,
                                    accountEmail,
                                    accountPassword,
                                    sellerNote
                                )
                            }
                        }
                    ) {
                        Text("Kirim Akun")
                    }

                } else {
                    Text("Menunggu seller mengirim akun")
                }
            }

            "account_sent" -> {
                if (role == "pembeli") {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Email :")
                            Text(transaction.account_email ?: "-")

                            Text("Password :")
                            Text(transaction.account_password ?: "-")

                            if (!transaction.seller_note.isNullOrEmpty()) {
                                Text("Catatan :")
                                Text(transaction.seller_note!!)
                            }
                        }
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (token != null) {
                                viewModel.completeTransaction(
                                    token,
                                    transactionId
                                )
                            }
                        }
                    ) {
                        Text("Selesaikan Transaksi")
                    }

                } else {
                    Text("Akun sudah dikirim ke pembeli")
                }
            }

            "completed" -> {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Transaksi selesai ✅",
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }

        if (viewModel.uploadMessage.isNotEmpty()) {
            Text(viewModel.uploadMessage)
        }

        if (viewModel.accountMessage.isNotEmpty()) {
            Text(viewModel.accountMessage)
        }
    }
}