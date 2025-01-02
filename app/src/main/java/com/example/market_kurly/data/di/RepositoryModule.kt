package com.example.market_kurly.data.di

import com.example.market_kurly.data.service.GoodsService
import com.example.market_kurly.data.service.LikeService
import com.example.market_kurly.data.service.ProductService
import com.example.market_kurly.data.service.ReviewService
import com.example.market_kurly.data.service.WishListService
import com.example.market_kurly.domain.repository.GoodsRepository
import com.example.market_kurly.domain.repository.LikeRepository
import com.example.market_kurly.domain.repository.ProductsRepository
import com.example.market_kurly.domain.repository.ReviewRepository
import com.example.market_kurly.domain.repository.WishListRepository
import com.example.market_kurly.domain.repositoryimpl.GoodsRepositoryImpl
import com.example.market_kurly.domain.repositoryimpl.LikeRepositoryImpl
import com.example.market_kurly.domain.repositoryimpl.ProductsRepositoryImpl
import com.example.market_kurly.domain.repositoryimpl.ReviewRepositoryImpl
import com.example.market_kurly.domain.repositoryimpl.WishListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGoodsRepository(service: GoodsService): GoodsRepository {
        return GoodsRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideLikeRepository(service: LikeService): LikeRepository {
        return LikeRepositoryImpl(service)
    }
    @Provides
    @Singleton
    fun provideProductRepository(service: ProductService): ProductsRepository {
        return ProductsRepositoryImpl(service)
    }
    @Provides
    @Singleton
    fun provideReviewRepository(service: ReviewService): ReviewRepository {
        return ReviewRepositoryImpl(service)
    }
    @Provides
    @Singleton
    fun provideWishListRepository(service: WishListService): WishListRepository {
        return WishListRepositoryImpl(service)
    }
}