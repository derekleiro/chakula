/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.chakula.ui

import Product
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chakula.ChakulaApplication
import com.example.chakula.data.AppContainer
import com.example.chakula.ui.checkout.CheckoutRoute
import com.example.chakula.ui.home.HomeRoute
import com.example.chakula.ui.home.HomeViewModel
import com.example.chakula.ui.orders.OrdersRoute
import com.example.chakula.ui.success.SuccessRoute

@Composable
fun ChakulaNavGraph(
    appContainer: AppContainer,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = ChakulaDestinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ChakulaDestinations.HOME_ROUTE) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(appContainer.productsRepository)
            )
            val sharedPreferences = remember {
                val context = ChakulaApplication.getContext()
                context?.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)
            }

            HomeRoute(
                navController = navController,
                homeViewModel = homeViewModel,
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
                addToCart =  fun (newProduct: Product) {
                    if (sharedPreferences != null) {
                        homeViewModel.addToCart(newProduct, sharedPreferences)
                    }
                }
            )
        }
        composable(ChakulaDestinations.CHECKOUT_ROUTE) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(appContainer.productsRepository)
            )
            val sharedPreferences = remember {
                val context = ChakulaApplication.getContext()
                context?.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)
            }
            CheckoutRoute(
                navController = navController,
                homeViewModel = homeViewModel,
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
                placeOrder = fun () {
                    if (sharedPreferences != null) {
                        homeViewModel.placeOrder(sharedPreferences)
                    }
                },
                removeFromCart = fun (newProduct: Product) {
                    if (sharedPreferences != null) {
                        homeViewModel.removeFromCart(newProduct, sharedPreferences)
                    }
                }
            )
        }

        composable(ChakulaDestinations.ORDERS_HISTORY_ROUTE) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(appContainer.productsRepository)
            )
            OrdersRoute(
                navController = navController,
                homeViewModel = homeViewModel,
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
            )
        }

        composable(ChakulaDestinations.SUCCESS_ROUTE) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(appContainer.productsRepository)
            )
            SuccessRoute(
                navController = navController,
                homeViewModel = homeViewModel,
            )
        }
    }
}
