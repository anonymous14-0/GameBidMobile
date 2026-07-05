package com.jemi.gamebidmobile.ui.item

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.utils.uriToFile
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateItemScreen(
    viewModel: AuctionViewModel = viewModel()
) {
    val context = LocalContext.current

    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()

    LaunchedEffect(Unit) {
        if (token != null) {
            viewModel.loadCategories(token)
        }
    }

    var title by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableIntStateOf(0) }
    var selectedCategoryName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            selectedImageUri = uri
        }

    LaunchedEffect(viewModel.itemMessage) {
        if (viewModel.itemMessage == "Item berhasil dibuat") {
            title = ""
            selectedCategoryId = 0
            selectedCategoryName = ""
            description = ""
            price = ""
            selectedImageUri = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Create Item",
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

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Item") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = {
                        expandedCategory = !expandedCategory
                    }
                ) {
                    OutlinedTextField(
                        value = selectedCategoryName,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = {
                            Text("Pilih Kategori")
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = {
                            expandedCategory = false
                        }
                    ) {
                        viewModel.categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Text(category.name)
                                },
                                onClick = {
                                    selectedCategoryId = category.id
                                    selectedCategoryName = category.name
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Harga") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pilih Foto")
                }

                selectedImageUri?.let {
                    Spacer(modifier = Modifier.height(16.dp))

                    AsyncImage(
                        model = it,
                        contentDescription = "Preview Foto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    enabled = !viewModel.isCreatingItem,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (
                            token != null &&
                            title.isNotEmpty() &&
                            selectedCategoryId != 0 &&
                            description.isNotEmpty() &&
                            price.isNotEmpty()
                        ) {

                            val imagePart =
                                if (selectedImageUri != null) {

                                    val file = uriToFile(
                                        context,
                                        selectedImageUri!!
                                    )

                                    val requestFile =
                                        file.asRequestBody(
                                            "image/*".toMediaTypeOrNull()
                                        )

                                    MultipartBody.Part.createFormData(
                                        "image",
                                        file.name,
                                        requestFile
                                    )
                                } else {
                                    null
                                }

                            viewModel.createItem(
                                token,
                                title.toRequestBody(),
                                selectedCategoryId
                                    .toString()
                                    .toRequestBody(),
                                description.toRequestBody(),
                                price.toRequestBody(),
                                imagePart
                            )
                        }
                    }
                ) {
                    if (viewModel.isCreatingItem) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Simpan Item")
                    }
                }
            }
        }

        if (viewModel.itemMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = viewModel.itemMessage,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}