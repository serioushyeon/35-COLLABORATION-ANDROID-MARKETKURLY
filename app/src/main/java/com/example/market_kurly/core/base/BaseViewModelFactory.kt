package com.example.market_kurly.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.market_kurly.data.ServicePool
import com.example.market_kurly.domain.repository.ExampleRepository
import com.example.market_kurly.domain.repository.GoodsRepository
import com.example.market_kurly.domain.repository.LikeRepository
import com.example.market_kurly.domain.repository.ReviewRepository
import com.example.market_kurly.domain.repository.WishListRepository
import com.example.market_kurly.domain.repositoryimpl.ReviewRepositoryImpl
import com.example.market_kurly.domain.repositoryimpl.WishListRepositoryImpl
import com.example.market_kurly.domain.repository.ProductsRepository
import com.example.market_kurly.domain.repositoryimpl.GoodsRepositoryImpl
import com.example.market_kurly.domain.repositoryimpl.LikeRepositoryImpl
import com.example.market_kurly.domain.repositoryimpl.ProductsRepositoryImpl
import com.example.market_kurly.feature.ExampleViewModel
import com.example.market_kurly.feature.goods.viewmodel.GoodsViewModel
import com.example.market_kurly.feature.review.ReviewViewModel
import com.example.market_kurly.feature.wishlist.WishListViewModel
import com.example.market_kurly.feature.home.HomeViewModel
import javax.inject.Inject

class BaseViewModelFactory @Inject constructor(
    private val exampleRepository: ExampleRepository? = null,
    private val goodsRepository: GoodsRepository? = null,
    private val likeRepository: LikeRepository? = null,
    private val productsRepository: ProductsRepository? = null,
    private val reviewRepository: ReviewRepository? = null,
    private val wishListRepository: WishListRepository ?= null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ExampleViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                exampleRepository?.let { ExampleViewModel(it) } as T
            }

            modelClass.isAssignableFrom(GoodsViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                GoodsViewModel(
                    goodsRepository ?: GoodsRepositoryImpl(ServicePool.goodsService),
                    likeRepository ?: LikeRepositoryImpl(ServicePool.likeService)
                ) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HomeViewModel(
                    productsRepository ?: ProductsRepositoryImpl(ServicePool.productsService),
                ) as T
            }

            modelClass.isAssignableFrom(ReviewViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                ReviewViewModel(
                    reviewRepository ?: ReviewRepositoryImpl(ServicePool.reviewService),
                    goodsRepository ?: GoodsRepositoryImpl(ServicePool.goodsService)
                ) as T
            }

            modelClass.isAssignableFrom(WishListViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                WishListViewModel(
                    wishListRepository ?: WishListRepositoryImpl(ServicePool.wishListService)
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
