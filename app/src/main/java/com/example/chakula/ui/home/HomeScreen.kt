package com.example.chakula.ui.home

import Product
import ProductCard
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chakula.R
import com.example.chakula.ui.ChakulaDestinations
import com.example.chakula.ui.ChakulaLogo
import com.example.chakula.ui.components.ChakulaSnackbarHost
import com.example.chakula.ui.rememberContentPaddingForScreen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * The home screen displaying just the article feed.
 */
@Composable
fun HomeFeedScreen(
    uiState: HomeUiState,
    navController: NavHostController,
    showTopAppBar: Boolean,
    onSelectProduct: (Product) -> Unit,
    onRefreshProducts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    openDrawer: () -> Unit,
    homeListLazyListState: LazyListState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    HomeScreenWithList(
        uiState = uiState,
        navController = navController,
        onRefreshProducts = onRefreshProducts,
        onErrorDismiss = onErrorDismiss,
        openDrawer = openDrawer,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) { hasPostsUiState, contentModifier ->
        ProductList(
            productFeed = hasPostsUiState.productsFeed,
            onProductTapped = onSelectProduct,
            contentPadding = rememberContentPaddingForScreen(
                additionalTop = if (showTopAppBar) 0.dp else 8.dp,
                excludeTop = showTopAppBar
            ),
            modifier = contentModifier,
            state = homeListLazyListState,
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenWithList(
    uiState: HomeUiState,
    navController: NavHostController,
    onRefreshProducts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    openDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    hasPostsContent: @Composable (
        uiState: HomeUiState.HasProducts,
        modifier: Modifier
    ) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        snackbarHost = { ChakulaSnackbarHost(hostState = snackbarHostState) },
        topBar = {
                HomeTopAppBar(
                    openDrawer = openDrawer,
                    topAppBarState = topAppBarState
                )
        },
        bottomBar = { CheckoutButton(navController)},
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)

        LoadingContent(
            empty = when (uiState) {
                is HomeUiState.HasProducts -> false
                is HomeUiState.NoProducts -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshProducts,
            content = {
                when (uiState) {
                    is HomeUiState.HasProducts -> hasPostsContent(uiState, contentModifier)
                    is HomeUiState.NoProducts -> {
                        if (uiState.errorMessages.isEmpty()) {
                            // if there are no products, and no error, let the user refresh manually
                            TextButton(
                                onClick = onRefreshProducts,
                                modifier.fillMaxSize()
                            ) {
                                Text(
                                    stringResource(id = R.string.home_tap_to_load_content),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            // there's currently an error showing, don't show any content
                            Box(contentModifier.fillMaxSize()) { /* empty screen */ }
                        }
                    }
                }
            }
        )
    }

    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }

        val errorMessageText: String = stringResource(errorMessage.messageId)
        val retryMessageText = stringResource(id = R.string.retry)

        val onRefreshProductsState by rememberUpdatedState(onRefreshProducts)
        val onErrorDismissState by rememberUpdatedState(onErrorDismiss)

        LaunchedEffect(errorMessageText, retryMessageText, snackbarHostState) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = errorMessageText,
                actionLabel = retryMessageText
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                onRefreshProductsState()
            }
            onErrorDismissState(errorMessage.id)
        }
    }
}

@Composable
private fun ProductList(
    productFeed: List<Product>,
    onProductTapped: (Product) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        state = state
    ) {

        if (productFeed.isNotEmpty()) {
            item { ProductListSection(productFeed, onProductTapped) }
        }
    }
}

@Composable
private fun CheckoutButton(navController: NavHostController) {
    Box(
        Modifier.padding(16.dp)
    ) {
        Button(
            onClick = { navController.navigate(ChakulaDestinations.CHECKOUT_ROUTE) },
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(text = "Checkout")
        }
    }
}

/**
 * Full screen circular progress indicator
 */
@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
) {
    CenterAlignedTopAppBar(
        title = {
            ChakulaLogo()
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}


@Composable
private fun ProductListSection(
    products: List<Product>,
    addToCart: (Product) -> Unit
) {
    Column {
        products.forEach { product ->
            ProductCard(product, addToCart)
            PostListDivider()
        }
    }
}


@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = onRefresh,
            content = content,
        )
    }
}


@Composable
private fun PostListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}
