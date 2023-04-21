
package com.example.chakula.ui.checkout

import Payment
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
fun CheckoutRoute(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    removeFromCart: (Product) -> Unit,
    paymentTypeSelected: (Payment) -> Unit,
    openDrawer: () -> Unit,
    placeOrder: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val homeListLazyListState = rememberLazyListState()

    CheckoutFeedScreen(
        uiState = uiState,
        navController = navController,
        showTopAppBar = !isExpandedScreen,
        onRefreshProducts = {homeViewModel.refreshProducts()},
        onErrorDismiss = {homeViewModel.errorShown(it)},
        openDrawer = openDrawer,
        placeOrder = placeOrder,
        homeListLazyListState = homeListLazyListState,
        onProductTapped = removeFromCart,
        paymentTypeSelected = paymentTypeSelected,
        snackbarHostState = snackbarHostState,
    )
}

