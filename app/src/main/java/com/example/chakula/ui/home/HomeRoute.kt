
package com.example.chakula.ui.home

import Product
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.navigation.NavHostController

@Composable
fun HomeRoute(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    addToCart: (Product) -> Unit,
    openDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val homeListLazyListState = rememberLazyListState()

    HomeFeedScreen(
        uiState = uiState,
        navController = navController,
        showTopAppBar = !isExpandedScreen,
        onSelectProduct = addToCart,
        onRefreshProducts = {homeViewModel.refreshProducts()},
        onErrorDismiss = {homeViewModel.errorShown(it)},
        openDrawer = openDrawer,
        homeListLazyListState = homeListLazyListState,
        snackbarHostState = snackbarHostState,
    )
}

