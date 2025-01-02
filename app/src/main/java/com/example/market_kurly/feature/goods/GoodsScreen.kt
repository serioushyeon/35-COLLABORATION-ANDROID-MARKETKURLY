package com.example.market_kurly.feature.goods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.market_kurly.R
import com.example.market_kurly.core.base.BaseViewModelFactory
import com.example.market_kurly.core.designsystem.component.KurlyGoodsDetailBottomBar
import com.example.market_kurly.core.designsystem.component.KurlyGoodsDetailTopBar
import com.example.market_kurly.core.util.KeyStorage.EMPTY_RESPONSE
import com.example.market_kurly.core.util.KeyStorage.GOODS
import com.example.market_kurly.core.util.KeyStorage.MEMBERSHIP_EXPAND
import com.example.market_kurly.core.util.KeyStorage.REVIEW
import com.example.market_kurly.core.util.KeyStorage.WISHLIST
import com.example.market_kurly.core.util.price.toDecimalFormat
import com.example.market_kurly.core.util.viewmodelfactory.hiltViewModelWithFactory
import com.example.market_kurly.feature.goods.component.KurlyAlsoViewedColumnItem
import com.example.market_kurly.feature.goods.component.KurlyGoodsInfoText
import com.example.market_kurly.feature.goods.component.KurlyGoodsMembershipToggleButton
import com.example.market_kurly.feature.goods.contract.GoodsContract
import com.example.market_kurly.feature.goods.viewmodel.GoodsViewModel
import com.example.market_kurly.ui.theme.Gray2
import com.example.market_kurly.ui.theme.Gray4
import com.example.market_kurly.ui.theme.Gray5
import com.example.market_kurly.ui.theme.Gray7
import com.example.market_kurly.ui.theme.MarketKurlyTheme.typography
import com.example.market_kurly.ui.theme.PrimaryColor400
import com.example.market_kurly.ui.theme.Red
import com.example.market_kurly.ui.theme.White

@Composable
fun GoodsScreen(
    navController: NavHostController,
    productId: Int
) {
    val context = LocalContext.current
    val factory = BaseViewModelFactory()
    val viewModel: GoodsViewModel = hiltViewModelWithFactory(factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val memberId = 1

    LaunchedEffect(Unit) {
        viewModel.sendEvent(
            GoodsContract.GoodsUiEvent.LoadGoodsDetail(
                memberId,
                productId
            )
        )
        viewModel.effect.collect { effect ->
            when (effect) {
                is GoodsContract.GoodsUiEffect.ShowSnackBar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(effect.message),
                        actionLabel = context.getString(R.string.goods_snackbar_action_go_to_wishlist),
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.sendEvent(GoodsContract.GoodsUiEvent.NavigateToWishlist)
                    }
                }

                GoodsContract.GoodsUiEffect.NavigateToWishlist -> navController.navigate(WISHLIST)
                GoodsContract.GoodsUiEffect.NavigateToGoodsDetail -> navController.navigate(GOODS) {
                    popUpTo(GOODS) { inclusive = true }
                    launchSingleTop = true
                }
                GoodsContract.GoodsUiEffect.NavigateToReview -> navController.navigate(
                    "review/${productId}"
                ) {
                    popUpTo(REVIEW) { inclusive = true }
                    launchSingleTop = true
                }
                GoodsContract.GoodsUiEffect.NavigateUp -> navController.navigateUp()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            KurlyGoodsDetailTopBar(
                goodsName = uiState.goodsDetails?.name.orEmpty(),
                selectedIndex = uiState.selectedTabIndex,
                navigateUp = {
                    navController.navigateUp()
                },
                navigateToGoodsDetail = {
                    viewModel.sendEvent(GoodsContract.GoodsUiEvent.NavigateToGoodsDetail)
                },
                navigateGoodsReview = {
                    viewModel.sendEvent(GoodsContract.GoodsUiEvent.NavigateToReview)
                },
            )
        },
        bottomBar = {
            KurlyGoodsDetailBottomBar(
                modifier = Modifier.background(White),
                isFavorite = uiState.isFavorite,
                onFavoriteClick = {
                    viewModel.sendEvent(
                        GoodsContract.GoodsUiEvent.OnFavoriteButtonPressed(
                            productId = productId,
                            memberId = memberId
                        )
                    )
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Gray2),
        ) {
            item {
                GoodsOverViewSection(uiState)
                Spacer(Modifier.height(8.dp))
            }

            item {
                AlsoViewedGoodsSection(uiState)
                Spacer(Modifier.height(8.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.goods_text_goods_information),
                    style = typography.bodyB16,
                    color = Gray7,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(15.dp, 14.dp),
                )
            }

            items(uiState.goodsInfoList) { info ->
                if (info.second != EMPTY_RESPONSE) {
                    KurlyGoodsInfoText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(White)
                            .padding(15.dp, 5.dp),
                        infoTitle = info.first,
                        infoContent = info.second,
                    )
                }
            }
        }
    }
}

@Composable
fun GoodsOverViewSection(uiState: GoodsContract.GoodsUiState) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(uiState.goodsDetails?.image)
            .build(),
        contentScale = ContentScale.FillWidth,
        contentDescription = uiState.goodsDetails?.name.orEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.777f),
    )
    Column(
        modifier = Modifier
            .background(White)
            .padding(start = 15.dp, top = 25.dp, end = 15.dp, bottom = 7.dp),
    ) {
        Text(
            text = uiState.goodsDetails?.deliverType.orEmpty(),
            style = typography.bodyB14,
            color = Gray5,
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(60.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = uiState.goodsDetails?.name.orEmpty(),
                style = typography.titleM18,
                color = Gray7,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icn_goods_share),
                contentDescription = stringResource(R.string.goods_icon_share_description),
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(
                R.string.goods_text_origin,
                uiState.goodsDetails?.origin.orEmpty(),
            ),
            style = typography.titleM18,
            color = Gray7,
        )

        Spacer(Modifier.height(14.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(
                    R.string.goods_text_price,
                    uiState.goodsDetails?.price?.toDecimalFormat().toString(),
                ),
                textDecoration = TextDecoration.LineThrough,
                style = typography.bodyR16,
                color = Gray4,
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icn_goods_info),
                contentDescription = stringResource(R.string.goods_icon_more_information_description),
                tint = Gray4,
                modifier = Modifier.size(18.dp),
            )
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(
                    R.string.goods_text_percent,
                    uiState.goodsDetails?.discount.toString(),
                ),
                style = typography.titleB22,
                color = Red,
            )
            Text(
                text = stringResource(
                    R.string.goods_text_price,
                    uiState.goodsDetails
                        ?.discountedPrice
                        ?.toDecimalFormat().orEmpty(),
                ),
                style = typography.titleB22,
                color = Gray7,
            )
        }

        Spacer(Modifier.height(3.dp))

        KurlyGoodsMembershipToggleButton(
            discount = uiState.goodsDetails?.membersDiscount ?: 0,
            price = uiState.goodsDetails?.membersDiscountedPrice ?: 0,
            itemId = MEMBERSHIP_EXPAND,
        )
    }

    Spacer(Modifier.height(1.dp))

    Column(
        modifier = Modifier
            .background(White)
            .padding(15.dp, 17.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        KurlyGoodsInfoText(
            modifier = Modifier.fillMaxWidth(),
            infoTitle = stringResource(R.string.goods_text_delivery),
            infoContent = uiState.goodsDetails?.deliverType.orEmpty(),
            infoSubContent = stringResource(R.string.goods_text_delivery_sub_description),
        )
        KurlyGoodsInfoText(
            modifier = Modifier.fillMaxWidth(),
            infoTitle = stringResource(R.string.goods_text_seller),
            infoContent = uiState.goodsDetails?.seller.orEmpty(),
        )
    }
}

@Composable
fun AlsoViewedGoodsSection(uiState: GoodsContract.GoodsUiState) {
    Column(
        modifier = Modifier
            .background(White)
            .padding(15.dp, 21.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.goods_text_also_viewed),
                style = typography.bodyB16,
                color = Gray7,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.goods_text_next_goods),
                    style = typography.bodyM16,
                    color = PrimaryColor400,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icn_goods_nextgoods),
                    contentDescription = stringResource(R.string.goods_icon_next_goods_description),
                    tint = PrimaryColor400,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Spacer(Modifier.height(7.dp))

        uiState.alsoViewedList.forEach { item ->
            KurlyAlsoViewedColumnItem(
                image = item.image,
                goodsName = item.goodsName,
                discount = item.discount,
                price = item.price,
            )
        }
    }
}
