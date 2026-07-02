package com.jemi.gamebidmobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemi.gamebidmobile.data.model.AuctionModel
import com.jemi.gamebidmobile.data.repository.AuctionRepository
import kotlinx.coroutines.launch

class AuctionViewModel : ViewModel() {

    private val repository = AuctionRepository()

    var auctions by mutableStateOf<List<AuctionModel>>(emptyList())
        private set
    var bidMessage by mutableStateOf("")
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
                loadAuctions() // refresh data
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
}