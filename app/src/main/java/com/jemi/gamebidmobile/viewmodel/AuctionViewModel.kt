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

    fun loadAuctions() {
        viewModelScope.launch {
            auctions = repository
                .getAuctions()
                .data
        }
    }
}