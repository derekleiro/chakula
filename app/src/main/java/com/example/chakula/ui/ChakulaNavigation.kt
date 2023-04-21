
package com.example.chakula.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Destinations used in the [ChakulaApp].
 */
object ChakulaDestinations {
    const val HOME_ROUTE = "home"
    const val ORDERS_HISTORY_ROUTE = "orders"
    const val CHECKOUT_ROUTE = "checkout"
    const val SUCCESS_ROUTE = "success"
}


class ChakulaNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(ChakulaDestinations.HOME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToOrders: () -> Unit = {
        navController.navigate(ChakulaDestinations.ORDERS_HISTORY_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToCheckout: () -> Unit = {
        navController.navigate(ChakulaDestinations.CHECKOUT_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSuccess: () -> Unit = {
        navController.navigate(ChakulaDestinations.SUCCESS_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
