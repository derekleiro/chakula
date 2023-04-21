
package com.example.chakula.ui.orders

import Product
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.navigation.NavHostController
import com.example.chakula.ui.home.HomeViewModel

@Composable
fun OrdersRoute(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val homeListLazyListState = rememberLazyListState()

    OrdersFeedScreen(
        uiState = uiState,
        navController = navController,
        showTopAppBar = !isExpandedScreen,
        onRefreshProducts = {homeViewModel.refreshProducts()},
        onErrorDismiss = {homeViewModel.errorShown(it)},
        openDrawer = openDrawer,
        homeListLazyListState = homeListLazyListState,
        snackbarHostState = snackbarHostState,
    )
}

