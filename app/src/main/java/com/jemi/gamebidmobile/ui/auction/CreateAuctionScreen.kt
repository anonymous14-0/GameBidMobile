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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAuctionScreen(
    viewModel: AuctionViewModel = viewModel()
) {
    val context = LocalContext.current

    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()

    var expanded by remember { mutableStateOf(false) }
    var selectedItemId by remember { mutableIntStateOf(0) }
    var selectedItemTitle by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var localMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (token != null) {
            viewModel.loadSellerItems(token)
        }
    }

    LaunchedEffect(viewModel.createMessage) {
        if (viewModel.createMessage == "Auction berhasil dibuat") {
            selectedItemId = 0
            selectedItemTitle = ""
            startTime = ""
            endTime = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                    modifier = Modifier.fillMaxWidth(),
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
                    Text("Buat Auction")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (localMessage.isNotEmpty()) {
            Text(
                text = localMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (viewModel.createMessage.isNotEmpty()) {
            Text(
                text = viewModel.createMessage,
                color = MaterialTheme.colorScheme.primary
            )
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