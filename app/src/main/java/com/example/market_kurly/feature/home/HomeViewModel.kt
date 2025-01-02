package com.example.market_kurly.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market_kurly.domain.repository.ProductsRepository
import com.example.market_kurly.feature.home.state.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor (
    private val productsRepository: ProductsRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        getHomeProductsData()
    }

    private fun getHomeProductsData() {
        viewModelScope.launch {
            productsRepository.getProducts()
                .onSuccess { products ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isSuccess = true,
                            mainTopData = products.mainTopProducts,
                            mainMiddleData = products.mainMiddleProducts,
                            mainBottomData = products.mainBottomData,
                        )
                    }
                }
                .onFailure {
                    Timber.tag("getHomeProductsData").e(it)
                }
        }
    }
}