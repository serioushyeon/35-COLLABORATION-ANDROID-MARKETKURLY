package com.example.market_kurly.domain.repositoryimpl

import com.example.market_kurly.data.mapper.toHomeUiData
import com.example.market_kurly.data.service.ProductService
import com.example.market_kurly.domain.model.HomeUiData
import com.example.market_kurly.domain.repository.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductsRepository {
    override suspend fun getProducts(): Result<HomeUiData> =
        runCatching {
            val response = productService.getHomeData()
            response.data?.toHomeUiData() ?: throw Exception("Response data is null")
        }
}