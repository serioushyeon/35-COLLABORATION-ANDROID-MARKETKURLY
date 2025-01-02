package com.example.market_kurly.data.di

import com.example.market_kurly.data.service.GoodsService
import com.example.market_kurly.data.service.LikeService
import com.example.market_kurly.data.service.ProductService
import com.example.market_kurly.data.service.ReviewService
import com.example.market_kurly.data.service.WishListService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideGoodsService(retrofit: Retrofit): GoodsService {
        return retrofit.create(GoodsService::class.java)
    }

    @Provides
    @Singleton
    fun provideLikeService(retrofit: Retrofit): LikeService {
        return retrofit.create(LikeService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductsService(retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewService(retrofit: Retrofit): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }

    @Provides
    @Singleton
    fun provideWishListService(retrofit: Retrofit): WishListService {
        return retrofit.create(WishListService::class.java)
    }
}