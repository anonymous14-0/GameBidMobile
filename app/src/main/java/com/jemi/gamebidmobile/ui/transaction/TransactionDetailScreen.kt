package com.jemi.gamebidmobile.ui.transaction

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.utils.uriToFile
import com.jemi.gamebidmobile.viewmodel.TransactionViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@Composable
fun TransactionDetailScreen(
    transactionId: Int,
    viewModel: TransactionViewModel = viewModel()
) {
    val context = LocalContext.current

    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()
    LaunchedEffect(Unit) {
        if (token != null) {
            viewModel.loadTransactionDetail(
                token,
                transactionId
            )
        }
    }
    var selectedUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var accountEmail by remember { mutableStateOf("") }
    var accountPassword by remember { mutableStateOf("") }
    var sellerNote by remember { mutableStateOf("") }

    // sementara status dummy
    val transaction =
        viewModel.selectedTransaction

    val transactionStatus =
        transaction?.status ?: "loading"

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            selectedUri = uri
        }
    if (transaction == null) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Loading...")
        }
        return
    }
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Detail Transaksi #$transactionId",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Status: $transactionStatus")

        Spacer(modifier = Modifier.height(16.dp))

        when (transactionStatus) {

            "pending_payment" -> {
                Button(
                    onClick = {
                        launcher.launch("image/*")
                    }
                ) {
                    Text("Pilih Gambar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
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
            }

            "payment_verified", "escrow" -> {
                OutlinedTextField(
                    value = accountEmail,
                    onValueChange = { accountEmail = it },
                    label = { Text("Email Akun") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = accountPassword,
                    onValueChange = { accountPassword = it },
                    label = { Text("Password Akun") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = sellerNote,
                    onValueChange = { sellerNote = it },
                    label = { Text("Catatan Seller") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
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
            }

            "account_sent" -> {
                Text("Akun sudah dikirim seller")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
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
            }

            "completed" -> {
                Text("Transaksi selesai ✅")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(viewModel.uploadMessage)
        Text(viewModel.accountMessage)
    }
}