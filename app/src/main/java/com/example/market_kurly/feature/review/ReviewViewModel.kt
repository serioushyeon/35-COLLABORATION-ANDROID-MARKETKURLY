package com.example.market_kurly.feature.review

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market_kurly.R
import com.example.market_kurly.domain.model.ReviewUiData
import com.example.market_kurly.domain.repository.GoodsRepository
import com.example.market_kurly.domain.repository.ReviewRepository
import com.example.market_kurly.feature.review.state.ReviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor (
    private val reviewRepository: ReviewRepository,
    private val goodsRepository: GoodsRepository
) : ViewModel() {

    private val _reviews = MutableStateFlow<List<ReviewUiData>>(emptyList())
    val reviews: StateFlow<List<ReviewUiData>> = _reviews

    @StringRes private val _snackbarMessage = MutableSharedFlow<Int>()
    @StringRes val snackbarMessage: SharedFlow<Int> = _snackbarMessage

    private val _navigateToWishlist = MutableSharedFlow<Unit>()
    val navigateToWishlist: SharedFlow<Unit> = _navigateToWishlist

    private var _uiState = MutableStateFlow(ReviewState())
    val uiState = _uiState.asStateFlow()

    fun getProductReviewsData(productId: Number) {
        viewModelScope.launch {
            reviewRepository.getProductReviews(productId)
            .onSuccess { reviewList ->
                _reviews.value = reviewList
            }.onFailure {
                Timber.tag("getProductReviewsData").e(it)
            }
        }
    }

    fun getGoodsDetailData(productId: Int) {
        viewModelScope.launch {
            goodsRepository.getGoodsDetailById(productId, 1)
                .onSuccess { goodsData ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            goodsDetails = goodsData
                        )
                    }
                }
                .onFailure {
                    Timber.tag("getGoodsDetailData").e(it)
                }
        }
    }

    fun toggleFavorite() {
        _uiState.update { currentState ->
            currentState.copy(isFavorite = !_uiState.value.isFavorite)
        }
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                _snackbarMessage.emit(R.string.goods_snackbar_message_favorite)
            }
        }
    }

    fun navigateToWishlist() {
        viewModelScope.launch {
            _navigateToWishlist.emit(Unit)
        }
    }
}