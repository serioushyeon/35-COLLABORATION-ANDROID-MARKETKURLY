package com.example.market_kurly.feature.goods.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.market_kurly.R
import com.example.market_kurly.core.base.BaseViewModel
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
import com.example.market_kurly.feature.goods.contract.GoodsContract.GoodsUiEffect
import com.example.market_kurly.feature.goods.contract.GoodsContract.GoodsUiEvent
import com.example.market_kurly.feature.goods.contract.GoodsContract.GoodsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoodsViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository,
    private val likeRepository: LikeRepository,
)  : BaseViewModel<GoodsUiState, GoodsUiEvent, GoodsUiEffect>(GoodsUiState()) {
    override fun reduceState(event: GoodsUiEvent) {
        when (event) {
            is GoodsUiEvent.LoadGoodsDetail -> getGoodsDetailData(
                memberId = event.memberId,
                productId = event.productId
            )
            is GoodsUiEvent.OnFavoriteButtonPressed -> toggleFavorite(
                memberId = event.memberId,
                productId = event.productId
            )
            GoodsUiEvent.NavigateToWishlist -> postEffect(GoodsUiEffect.NavigateToWishlist)
            GoodsUiEvent.NavigateToGoodsDetail -> postEffect(GoodsUiEffect.NavigateToGoodsDetail)
            GoodsUiEvent.NavigateToReview -> postEffect(GoodsUiEffect.NavigateToReview)
            GoodsUiEvent.NavigateUp -> postEffect(GoodsUiEffect.NavigateUp)
        }
    }

    private fun getGoodsDetailData(productId: Int, memberId: Int) {
        viewModelScope.launch {
            goodsRepository.getGoodsDetailById(productId, memberId)
                .onSuccess { goodsData ->
                    updateState(
                        currentState.copy(
                            isSuccess = true,
                            alsoViewedList = goodsRepository.getDummyAlsoViewedList(),
                            goodsDetails = goodsData,
                            goodsInfoList = createInfoPairs(goodsData?.infoData),
                            isFavorite = goodsData?.isInterest ?: false,
                        )
                    )
                }
                .onFailure {
                    updateState(
                        currentState.copy(
                            isSuccess = false,
                            alsoViewedList = goodsRepository.getDummyAlsoViewedList(),
                        )
                    )
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

    private fun toggleFavorite(productId: Int, memberId: Int) {
        viewModelScope.launch {
            updateState(
                currentState.copy(isFavorite = !currentState.isFavorite)
            )
            if (currentState.isFavorite) {
                likeRepository.postProductsLike(productId, memberId)
                    .onSuccess {
                        postEffect(GoodsUiEffect.ShowSnackBar(R.string.goods_snackbar_message_favorite))
                    }
                    .onFailure {
                        postEffect(GoodsUiEffect.ShowSnackBar(R.string.goods_snackbar_message_fail))
                    }
            } else {
                likeRepository.deleteProductsLike(productId, memberId)
                    .onFailure {
                        postEffect(GoodsUiEffect.ShowSnackBar(R.string.goods_snackbar_message_fail))
                    }
            }
        }
    }
}