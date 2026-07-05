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

    var loadErrorMessage by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var bidMessage by mutableStateOf("")
        private set

    var isSubmittingBid by mutableStateOf(false)
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
            isSubmittingBid = true
            bidMessage = ""
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
            } finally {
                isSubmittingBid = false
            }
        }
    }

    fun loadAuctions() {
        viewModelScope.launch {
            isLoading = true
            loadErrorMessage = ""
            try {
                auctions = repository
                    .getAuctions()
                    .data
            } catch (e: Exception) {
                loadErrorMessage = "Network error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadSellerAuctions(token: String) {
        viewModelScope.launch {
            isLoading = true
            loadErrorMessage = ""
            try {
                auctions = repository
                    .getSellerAuctions(token)
                    .data
            } catch (e: Exception) {
                loadErrorMessage = "Gagal load auction: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadSellerItems(token: String) {
        viewModelScope.launch {
            try {
                sellerItems = repository
                    .getSellerItems(token)
                    .data
            } catch (e: Exception) {
                loadErrorMessage = "Gagal load item: ${e.message}"
            }
        }
    }

    fun createAuction(
        token: String,
        itemId: Int,
        startTime: String,
        endTime: String
    ) {
        viewModelScope.launch {
            createMessage = ""
            isLoading = true
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
            } finally {
                isLoading = false
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
            isLoading = true
            loadErrorMessage = ""
            try {
                selectedAuction = repository
                    .getAuctionDetail(auctionId)
                    .data

            } catch (e: Exception) {
                loadErrorMessage = "Gagal load detail: ${e.message}"
            } finally {
                isLoading = false
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