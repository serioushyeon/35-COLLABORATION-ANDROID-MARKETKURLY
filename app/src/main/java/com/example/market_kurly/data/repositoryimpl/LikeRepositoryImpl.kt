package com.example.market_kurly.domain.repositoryimpl

import com.example.market_kurly.data.service.LikeService
import com.example.market_kurly.domain.repository.LikeRepository
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val likeService: LikeService,
) : LikeRepository {

    override suspend fun postProductsLike(productsId: Int, memberId: Int): Result<String?> = runCatching {
        val response = likeService.postProductLike(productsId, memberId)
        response.data
    }

    override suspend fun deleteProductsLike(productsId: Int, memberId: Int): Result<String?> = runCatching {
        val response = likeService.deleteProductLike(productsId, memberId)
        response.data
    }
}
