package com.example.market_kurly.feature.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.market_kurly.R
import com.example.market_kurly.core.designsystem.component.KurlyGoodsDetailBottomBar
import com.example.market_kurly.core.designsystem.component.KurlyGoodsDetailTopBar
import com.example.market_kurly.core.util.KeyStorage.GOODS
import com.example.market_kurly.core.util.KeyStorage.REVIEW
import com.example.market_kurly.core.util.KeyStorage.REVIEW_FILTER_LEAST_STARS
import com.example.market_kurly.core.util.KeyStorage.REVIEW_FILTER_MOST_STARS
import com.example.market_kurly.core.util.KeyStorage.REVIEW_FILTER_RECENT
import com.example.market_kurly.core.util.KeyStorage.WISHLIST
import com.example.market_kurly.feature.review.component.ReviewFilteringBar
import com.example.market_kurly.feature.review.component.ReviewImageRow
import com.example.market_kurly.feature.review.component.ReviewItem
import com.example.market_kurly.feature.review.component.ReviewNoticeItem
import com.example.market_kurly.ui.theme.Gray2
import com.example.market_kurly.ui.theme.Gray7
import com.example.market_kurly.ui.theme.MarketKurlyTheme
import com.example.market_kurly.ui.theme.White

@Composable
fun ReviewScreen(
    navController: NavHostController,
    productId: Int,
    modifier: Modifier = Modifier,
) {
    val viewModel: ReviewViewModel = hiltViewModel()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf(REVIEW_FILTER_RECENT) }
    val filteredReviews = remember(reviews, selectedFilter) {
        when (selectedFilter) {
            REVIEW_FILTER_RECENT -> reviews.sortedByDescending { it.createdAt }
            REVIEW_FILTER_MOST_STARS -> reviews.sortedByDescending { it.score }
            REVIEW_FILTER_LEAST_STARS -> reviews.sortedBy { it.score }
            else -> reviews
        }
    }

    LaunchedEffect(true) {
        viewModel.getGoodsDetailData(productId)
        viewModel.getProductReviewsData(productId)
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            val result = snackbarHostState.showSnackbar(
                message = context.getString(message),
                actionLabel = context.getString(R.string.goods_snackbar_action_go_to_wishlist),
                duration = SnackbarDuration.Short,
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.navigateToWishlist()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToWishlist.collect {
            navController.navigate(WISHLIST)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            KurlyGoodsDetailTopBar(
                goodsName = uiState.goodsName,
                selectedIndex = uiState.selectedTabIndex,
                navigateUp = {
                    navController.navigateUp()
                },
                navigateToGoodsDetail = {
                    navController.navigate("$GOODS/$productId") {
                        popUpTo(GOODS) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                navigateGoodsReview = {
                    navController.navigate(REVIEW) {
                        popUpTo(REVIEW) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        },
        bottomBar = {
            KurlyGoodsDetailBottomBar(
                modifier = Modifier.background(White),
                isFavorite = uiState.isFavorite,
                onFavoriteClick = { viewModel.toggleFavorite() },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(White)
                .padding(innerPadding)
        ) {
            ReviewNoticeItem(
                text = stringResource(R.string.review_notice_best_review)
            )

            Spacer(modifier = modifier.height(6.dp))

            ReviewNoticeItem(
                text = stringResource(R.string.review_notice_savings_rule)
            )

            Spacer(modifier = modifier.height(20.dp))

            Text(
                text = stringResource(R.string.review_photo_review),
                style = MarketKurlyTheme.typography.bodyM14,
                color = Gray7,
                modifier = modifier.padding(start = 16.dp)
            )

            val firstImages = reviews.flatMap { listOfNotNull(it.image1, it.image2) }
            ReviewImageRow(
                imageUrls = firstImages,
                modifier = modifier
                    .padding(start = 16.dp, top = 8.dp)
            )

            Spacer(modifier = modifier.height(15.dp))

            Spacer(
                modifier = modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(Gray2)
            )

            ReviewFilteringBar(
                onFilterSelected = { selectedFilter = it }
            )

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(White)
            ) {
                itemsIndexed(filteredReviews) { index, review ->

                    Column {
                        ReviewItem(
                            userName = review.userName,
                            productName = uiState.goodsName,
                            imageUrls = listOf(review.image1, review.image2, review.image3),
                            reviewText = review.content,
                            reviewDate = review.createdAt,
                            starCount = review.score.toInt(),
                            modifier = modifier.padding(bottom = 16.dp)
                        )

                        if (index < reviews.size - 1) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Gray2
                            )
                        }
                    }
                }
            }
        }
    }
}