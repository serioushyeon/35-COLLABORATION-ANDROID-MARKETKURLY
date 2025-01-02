package com.example.market_kurly.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.market_kurly.R
import com.example.market_kurly.core.util.modifier.noRippleClickable
import com.example.market_kurly.feature.home.component.AsyncImageFillWidth
import com.example.market_kurly.feature.home.component.HomeBannerRow
import com.example.market_kurly.feature.home.component.HomeBottomNav
import com.example.market_kurly.feature.home.component.HomeProductRow
import com.example.market_kurly.feature.home.component.HomeProductTitle
import com.example.market_kurly.feature.home.component.HomeRankingProductRow
import com.example.market_kurly.feature.home.component.HomeTagItemRow
import com.example.market_kurly.feature.home.component.HomeTopBar
import com.example.market_kurly.feature.home.dummy.HomeScreenData
import com.example.market_kurly.feature.home.dummy.bannerDummy
import com.example.market_kurly.ui.theme.CoolGray4
import com.example.market_kurly.ui.theme.GrGray1
import com.example.market_kurly.ui.theme.Gray8
import com.example.market_kurly.ui.theme.MARKETKURLYTheme
import com.example.market_kurly.ui.theme.MarketKurlyTheme
import com.example.market_kurly.ui.theme.White

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val viewModel: HomeViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val bannerList = bannerDummy()
    val tagMenuList = HomeScreenData()
    val rankingProducts = uiState.mainMiddleData
    val mainTopProducts = uiState.mainTopData
    val mainBottomProducts = uiState.mainBottomData
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (topBar, content, bottomNav) = createRefs()
        HomeTopBar(
            modifier = Modifier
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                }
        )
        Column(
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(topBar.bottom)
                    bottom.linkTo(bottomNav.top)
                    height = Dimension.fillToConstraints
                }
                .verticalScroll(rememberScrollState())
        ) {
            HomeBannerRow(bannerList)
            Spacer(modifier = Modifier.height(8.dp))
            HomeTagItemRow(tagMenuList)
            Spacer(modifier = Modifier.height(31.dp))
            HomeProductTitle(
                stringResource(id = R.string.home_firstProduct_title),
                stringResource(id = R.string.home_firstProduct_subTitle),
                modifier = Modifier
                   .padding(start = 15.dp, end = 9.dp)
            )
            Spacer(modifier = Modifier.height(17.dp))
            HomeProductRow(
                mainTopProducts,
                navController = navController
            )
            Spacer(modifier = Modifier.height(14.dp))
            AsyncImageFillWidth(
                imageUrl = "https://prod-files-secure.s3.us-west-2.amazonaws.com/01c30015-16dc-4e14-8e54-35fb1a5705fe/826939ec-c3d6-4b17-9258-c7170db2bf29/IMG_0136_1.png",
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                White,
                                GrGray1
                            ),
                            start = Offset(0f, 0f), // 시작점
                            end = Offset(0f, 1000f) // 끝점
                        )
                    )
            ) {
                Spacer(modifier = Modifier.height(44.dp))
                Text(
                    stringResource(id = R.string.home_rankingProduct_title),
                    style = MarketKurlyTheme.typography.titleB18,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Gray8
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    stringResource(id = R.string.home_rankingProduct_subTitle),
                    style = MarketKurlyTheme.typography.bodyR15,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = CoolGray4
                )
                Spacer(modifier = Modifier.height(24.dp))
                HomeRankingProductRow(
                    rankingProducts,
                )
                Spacer(modifier = Modifier.height(23.dp))
                HomeAllButton(
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            Spacer(modifier = Modifier.height(44.dp))
            HomeProductTitle(
                stringResource(id = R.string.home_secondProduct_title),
                stringResource(id = R.string.home_secondProduct_subTitle),
                modifier = Modifier
                    .padding(start = 15.dp, end = 9.dp)
            )
            Spacer(modifier = Modifier.height(17.dp))
            HomeProductRow(
                mainBottomProducts,
                navController = navController
            )
            Spacer(modifier = Modifier.height(28.dp))
        }
        HomeBottomNav(
            modifier = Modifier
                .constrainAs(bottomNav) {
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun HomeAllButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White, shape = RoundedCornerShape(4.dp))
            .padding(vertical = 14.dp)
            .noRippleClickable(onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            stringResource(id = R.string.home_all_view),
            style = MarketKurlyTheme.typography.bodyR14
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_home_arrow_right),
            contentDescription = stringResource(R.string.home_allShowBtn_description)
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MARKETKURLYTheme {
        HomeScreen(navController = NavHostController(LocalContext.current))
    }
}