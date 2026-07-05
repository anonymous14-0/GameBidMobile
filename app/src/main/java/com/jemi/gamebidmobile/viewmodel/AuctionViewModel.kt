package com.jemi.gamebidmobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemi.gamebidmobile.data.model.AuctionModel
import com.jemi.gamebidmobile.data.model.CategoryModel
import com.jemi.gamebidmobile.data.model.ItemModel
import com.jemi.gamebidmobile.data.repository.AuctionRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuctionViewModel : ViewModel() {

    private val repository = AuctionRepository()

    var auctions by mutableStateOf<List<AuctionModel>>(emptyList())
        private set

    var bidMessage by mutableStateOf("")
        private set

    var sellerItems by mutableStateOf<List<ItemModel>>(emptyList())
        private set

    var createMessage by mutableStateOf("")
        private set

    var itemMessage by mutableStateOf("")
        private set

    var categories by mutableStateOf<List<CategoryModel>>(emptyList())
        private set

    var selectedAuction by mutableStateOf<AuctionModel?>(null)
        private set

    var isCreatingItem by mutableStateOf(false)
        private set

    fun submitBid(
        token: String,
        auctionId: Int,
        bidAmount: Int
    ) {
        viewModelScope.launch {
            try {
                repository.submitBid(
                    token,
                    auctionId,
                    bidAmount
                )

                loadAuctions()
                bidMessage = "Bid berhasil"

            } catch (e: Exception) {
                bidMessage = "Bid gagal: ${e.message}"
            }
        }
    }

    fun loadAuctions() {
        viewModelScope.launch {
            auctions = repository
                .getAuctions()
                .data
        }
    }

    fun loadSellerAuctions(token: String) {
        viewModelScope.launch {
            auctions = repository
                .getSellerAuctions(token)
                .data
        }
    }

    fun loadSellerItems(token: String) {
        viewModelScope.launch {
            sellerItems = repository
                .getSellerItems(token)
                .data
        }
    }

    fun createAuction(
        token: String,
        itemId: Int,
        startTime: String,
        endTime: String
    ) {
        viewModelScope.launch {
            try {
                repository.createAuction(
                    token,
                    itemId,
                    startTime,
                    endTime
                )

                loadSellerAuctions(token)
                createMessage = "Auction berhasil dibuat"

            } catch (e: Exception) {
                createMessage = "Gagal: ${e.message}"
            }
        }
    }

    fun loadCategories(token: String) {
        viewModelScope.launch {
            categories = repository
                .getCategories(token)
                .data
        }
    }

    fun loadAuctionDetail(
        auctionId: Int
    ) {
        viewModelScope.launch {
            try {
                selectedAuction = repository
                    .getAuctionDetail(auctionId)
                    .data

            } catch (e: Exception) {
                bidMessage =
                    "Gagal load detail: ${e.message}"
            }
        }
    }

    fun createItem(
        token: String,
        title: RequestBody,
        categoryId: RequestBody,
        description: RequestBody,
        startingPrice: RequestBody,
        image: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            try {
                itemMessage = ""
                isCreatingItem = true

                repository.createItem(
                    token,
                    title,
                    categoryId,
                    description,
                    startingPrice,
                    image
                )

                loadSellerItems(token)

                itemMessage = "Item berhasil dibuat"

            } catch (e: Exception) {
                itemMessage = "Gagal: ${e.message}"
            } finally {
                isCreatingItem = false
            }
        }
    }
}