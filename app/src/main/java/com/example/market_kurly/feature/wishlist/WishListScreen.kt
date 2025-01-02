package com.example.market_kurly.feature.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.market_kurly.core.util.KeyStorage.WISHLIST_CATEGORY_DAIRY_PRODUCT
import com.example.market_kurly.core.util.KeyStorage.WISHLIST_CATEGORY_FRUIT_NUTS_RICE
import com.example.market_kurly.core.util.KeyStorage.WISHLIST_CATEGORY_SIMPLE_PRODUCT
import com.example.market_kurly.core.util.KeyStorage.WISHLIST_CATEGORY_SNACK
import com.example.market_kurly.core.util.KeyStorage.WISHLIST_CATEGORY_TOTAL
import com.example.market_kurly.feature.wishlist.component.WishListFilteringTab
import com.example.market_kurly.feature.wishlist.component.WishListProduct
import com.example.market_kurly.feature.wishlist.component.WishListTopBar
import com.example.market_kurly.ui.theme.MARKETKURLYTheme
import com.example.market_kurly.ui.theme.White

@Composable
fun WishListScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: WishListViewModel = hiltViewModel()
    val wishList by viewModel.wishListItems.collectAsStateWithLifecycle()

    var selectedCategory by remember { mutableStateOf(WISHLIST_CATEGORY_TOTAL) }

    LaunchedEffect(Unit) {
        viewModel.getWishListData(memberId = 1)
    }

    val filteredWishList = remember(wishList, selectedCategory) {
        when (selectedCategory) {
            WISHLIST_CATEGORY_TOTAL -> wishList
            WISHLIST_CATEGORY_DAIRY_PRODUCT -> wishList.filter { it.categoryScope == "DairyProduct" }
            WISHLIST_CATEGORY_SIMPLE_PRODUCT -> wishList.filter { it.categoryScope == "convenience" }
            WISHLIST_CATEGORY_FRUIT_NUTS_RICE -> wishList.filter { it.categoryScope == "FruitsNutsRice" }
            WISHLIST_CATEGORY_SNACK -> wishList.filter { it.categoryScope == "SnacksRicecakes" }
            else -> wishList
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        WishListTopBar(
            modifier = modifier
        )

        Spacer(modifier = Modifier.height(20.dp))

        WishListFilteringTab(
            modifier = modifier.fillMaxWidth(),
            onClick = { category -> selectedCategory = category }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredWishList) { wishListItem ->
                WishListProduct(
                    name = wishListItem.name,
                    image = wishListItem.image,
                    price = wishListItem.price,
                    discount = wishListItem.discount,
                    categoryScope = wishListItem.categoryScope
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WishListPreview() {
    MARKETKURLYTheme {
        WishListScreen(modifier = Modifier, navController = rememberNavController())
    }
}