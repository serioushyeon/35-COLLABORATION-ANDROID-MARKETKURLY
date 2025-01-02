package com.example.market_kurly.feature.goods.contract

import androidx.annotation.StringRes
import com.example.market_kurly.core.base.UiEffect
import com.example.market_kurly.core.base.UiEvent
import com.example.market_kurly.core.base.UiState
import com.example.market_kurly.core.util.KeyStorage.ALL_TABS
import com.example.market_kurly.core.util.KeyStorage.GOODS_DETAIL
import com.example.market_kurly.domain.model.AlsoViewedData
import com.example.market_kurly.domain.model.GoodsUiData

class GoodsContract {
    data class GoodsUiState(
        val isSuccess: Boolean = false,
        val message: String = "",
        val selectedTabIndex: Int = ALL_TABS.indexOf(GOODS_DETAIL),
        val isFavorite: Boolean = false,
        val alsoViewedList: List<AlsoViewedData> = emptyList(),
        val goodsDetails: GoodsUiData? = null,
        val goodsInfoList: List<Pair<String, String>> = emptyList(),
    ) : UiState

    sealed class GoodsUiEvent : UiEvent {
        data class LoadGoodsDetail(val productId: Int, val memberId: Int) : GoodsUiEvent()
        data class OnFavoriteButtonPressed(val productId: Int, val memberId: Int) : GoodsUiEvent()
        data object NavigateToWishlist : GoodsUiEvent()
        data object NavigateToReview : GoodsUiEvent()
        data object NavigateToGoodsDetail : GoodsUiEvent()
        data object NavigateUp : GoodsUiEvent()
    }

    sealed class GoodsUiEffect : UiEffect {
        data class ShowSnackBar(@StringRes val message: Int) : GoodsUiEffect()
        data object NavigateToWishlist : GoodsUiEffect()
        data object NavigateToReview : GoodsUiEffect()
        data object NavigateToGoodsDetail : GoodsUiEffect()
        data object NavigateUp : GoodsUiEffect()
    }
}