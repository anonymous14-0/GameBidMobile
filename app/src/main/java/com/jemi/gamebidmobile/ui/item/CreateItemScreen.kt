package com.jemi.gamebidmobile.ui.item

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel
import okhttp3.RequestBody.Companion.toRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateItemScreen(
    viewModel: AuctionViewModel = viewModel()
){
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
    var expandedCategory by remember {
        mutableStateOf(false)
    }

    var selectedCategoryId by remember {
        mutableIntStateOf(0)
    }

    var selectedCategoryName by remember {
        mutableStateOf("")
    }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Create Item",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul Item") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        @OptIn(ExperimentalMaterial3Api::class)
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
                modifier = Modifier.menuAnchor(),
                label = { Text("Pilih Kategori") }
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
            label = { Text("Deskripsi") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Harga") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (
                    token != null &&
                    title.isNotEmpty() &&
                    selectedCategoryId != 0 &&
                    description.isNotEmpty() &&
                    price.isNotEmpty()
                ) {
                    viewModel.createItem(
                        token,
                        title.toRequestBody(),
                        selectedCategoryId
                            .toString()
                            .toRequestBody(),
                        description.toRequestBody(),
                        price.toRequestBody(),
                        null
                    )
                }
            }
        ) {
            Text("Simpan Item")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(viewModel.itemMessage)

    }
}