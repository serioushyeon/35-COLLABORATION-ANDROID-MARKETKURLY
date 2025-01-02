package com.example.market_kurly.feature.goods.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market_kurly.R
import com.example.market_kurly.domain.model.GoodsInfoData
import com.example.market_kurly.core.util.KeyStorage.ALLERGY
import com.example.market_kurly.core.util.KeyStorage.BRIX
import com.example.market_kurly.core.util.KeyStorage.EXPIRATION
import com.example.market_kurly.core.util.KeyStorage.NOTIFICATION
import com.example.market_kurly.core.util.KeyStorage.PACKAGING_TYPE
import com.example.market_kurly.core.util.KeyStorage.SELLING_UNIT
import com.example.market_kurly.core.util.KeyStorage.WEIGHT
import com.example.market_kurly.domain.repository.GoodsRepository
import com.example.market_kurly.domain.repository.LikeRepository
import com.example.market_kurly.feature.goods.state.GoodsState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoodsViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository,
    private val likeRepository: LikeRepository,
) : ViewModel() {

    @StringRes
    private val _snackbarMessage = MutableSharedFlow<Int>()

    @StringRes
    val snackbarMessage: SharedFlow<Int> = _snackbarMessage

    private val _navigateToWishlist = MutableSharedFlow<Unit>()
    val navigateToWishlist: SharedFlow<Unit> = _navigateToWishlist

    private var _uiState = MutableStateFlow(GoodsState())
    val uiState = _uiState.asStateFlow()

    fun getGoodsDetailData(productId: Int, memberId: Int) {
        viewModelScope.launch {
            goodsRepository.getGoodsDetailById(productId, memberId)
                .onSuccess { goodsData ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isSuccess = true,
                            alsoViewedList = goodsRepository.getDummyAlsoViewedList(),
                            goodsDetails = goodsData,
                            goodsInfoList = createInfoPairs(goodsData?.infoData),
                            isFavorite = goodsData?.isInterest ?: false,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isSuccess = false,
                            alsoViewedList = goodsRepository.getDummyAlsoViewedList(),
                        )
                    }

                }
        }
    }

    private fun createInfoPairs(info: GoodsInfoData?): List<Pair<String, String>> {
        info?.let {
            return listOf(
                PACKAGING_TYPE to info.packagingType,
                SELLING_UNIT to info.sellingUnit,
                WEIGHT to info.weight,
                ALLERGY to info.allergy,
                EXPIRATION to info.expiration,
                BRIX to info.brix,
                NOTIFICATION to info.notification,
            )
        }
        return emptyList()
    }

    fun toggleFavorite(productId: Int, memberId: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isFavorite = !_uiState.value.isFavorite)
            }
            if (_uiState.value.isFavorite) {
                likeRepository.postProductsLike(productId, memberId)
                    .onSuccess {
                            _snackbarMessage.emit(R.string.goods_snackbar_message_favorite)
                    }
                    .onFailure {
                            _snackbarMessage.emit(R.string.goods_snackbar_message_fail)
                    }
            }
            else{
                likeRepository.deleteProductsLike(productId, memberId)
                    .onFailure {
                        _snackbarMessage.emit(R.string.goods_snackbar_message_fail)
                    }
            }
        }
    }

    fun navigateToWishlist() {
        viewModelScope.launch {
            _navigateToWishlist.emit(Unit)
        }
    }
}
