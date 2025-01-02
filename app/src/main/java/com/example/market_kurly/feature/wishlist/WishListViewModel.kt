package com.example.market_kurly.feature.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market_kurly.core.util.KeyStorage.WISHLIST_CATEGORY_TOTAL
import com.example.market_kurly.domain.model.WishListUiData
import com.example.market_kurly.domain.repository.WishListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor (
    private val wishListRepository: WishListRepository
) : ViewModel() {
    private val _wishListItems = MutableStateFlow<List<WishListUiData>>(emptyList())
    val wishListItems: StateFlow<List<WishListUiData>> = _wishListItems

    private val _selectedCategory = MutableStateFlow(WISHLIST_CATEGORY_TOTAL)
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun getWishListData(memberId: Number) {
        viewModelScope.launch {
            wishListRepository.getWishList(memberId)
                .onSuccess { wishListItems ->
                    _wishListItems.update { wishListItems }
                }
                .onFailure { error ->
                    _errorMessage.update { error.message }
                }
        }
    }
}