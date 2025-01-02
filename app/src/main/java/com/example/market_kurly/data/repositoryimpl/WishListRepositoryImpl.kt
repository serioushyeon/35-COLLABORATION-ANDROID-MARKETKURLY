package com.example.market_kurly.domain.repositoryimpl

import com.example.market_kurly.data.mapper.toWishListDataList
import com.example.market_kurly.data.service.WishListService
import com.example.market_kurly.domain.model.WishListUiData
import com.example.market_kurly.domain.repository.WishListRepository
import javax.inject.Inject

class WishListRepositoryImpl @Inject constructor(
    private val wishListService: WishListService
) : WishListRepository {
    override suspend fun getWishList(memberId: Number): Result<List<WishListUiData>> =
        runCatching {
            val response = wishListService.getWishList(memberId)
            response.data?.toWishListDataList() ?: emptyList()
        }
}