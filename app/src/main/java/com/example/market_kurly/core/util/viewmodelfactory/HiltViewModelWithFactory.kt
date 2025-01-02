package com.example.market_kurly.core.util.viewmodelfactory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

@Composable
inline fun <reified VM : ViewModel> hiltViewModelWithFactory(
    factory: ViewModelProvider.Factory
): VM {
    val owner = LocalViewModelStoreOwner.current
    return remember(factory) {
        ViewModelProvider(owner!!, factory).get(VM::class.java)
    }
}