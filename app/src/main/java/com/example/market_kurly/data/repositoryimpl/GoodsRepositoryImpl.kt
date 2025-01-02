package com.example.market_kurly.domain.repositoryimpl

import com.example.market_kurly.data.mapper.toGoodsUiData
import com.example.market_kurly.data.service.GoodsService
import com.example.market_kurly.domain.model.AlsoViewedData
import com.example.market_kurly.domain.model.GoodsUiData
import com.example.market_kurly.domain.repository.GoodsRepository
import okhttp3.internal.immutableListOf
import javax.inject.Inject

class GoodsRepositoryImpl @Inject constructor(
    private val goodsService: GoodsService
) : GoodsRepository {
    override fun getDummyAlsoViewedList(): List<AlsoViewedData> =
        immutableListOf(
            AlsoViewedData(
                "https://product-image.kurly.com/hdims/resize/%5E%3E720x%3E936/cropcenter/720x936/quality/85/src/product/image/59526651-9c34-4a39-9cd0-e30669a9ec4f.jpg",
                "[KF365] 유명산지 고당도사과 1.5kg (5~6입)",
                16,
                19900
            ),
            AlsoViewedData(
                "https://product-image.kurly.com/hdims/resize/%5E%3E720x%3E936/cropcenter/720x936/quality/85/src/product/image/d79e3f0a-2739-4d54-a5b3-3dbd1d5b4388.jpeg",
                "고랭지 햇사과 1.3kg (4~6입)",
                13,
                14900
            ),
            AlsoViewedData(
                "https://img-cf.kurly.com/hdims/resize/%5E%3E720x%3E936/cropcenter/720x936/quality/85/src/shop/data/goods/160342712083l0.jpg",
                "감홍 사과 1.3kg (4~6입)",
                25,
                19900
            ),
            AlsoViewedData(
                "https://product-image.kurly.com/hdims/resize/%5E%3E720x%3E936/cropcenter/720x936/quality/85/src/product/image/30b30de4-14f7-438a-844d-604b5a2acde9.jpg",
                "세척 사과 1.4kg (7입)",
                16,
                14900
            ),
        )

    override suspend fun getGoodsDetailById(productsId: Int, memberId: Int): Result<GoodsUiData?> =
        runCatching {
            val response = goodsService.getGoodsDetailById(productsId, memberId)
            response.data?.toGoodsUiData()
        }
}