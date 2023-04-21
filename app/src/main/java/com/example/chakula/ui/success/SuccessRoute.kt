
package com.example.chakula.ui.success

import SuccessScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.chakula.ui.home.HomeViewModel

@Composable
fun SuccessRoute(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
) {

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    SuccessScreen(uiState = uiState, navController = navController)
}