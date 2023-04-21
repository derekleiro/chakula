package com.example.chakula.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.StackedLineChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chakula.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToOrders: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier) {
        ChakulaLogo(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
        )
        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.home_title)) },
            icon = { Icon(Icons.Filled.Home, null) },
            selected = currentRoute == ChakulaDestinations.HOME_ROUTE,
            onClick = { navigateToHome(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.orders_title)) },
            icon = { Icon(Icons.Filled.List, null) },
            selected = currentRoute == ChakulaDestinations.ORDERS_HISTORY_ROUTE,
            onClick = { navigateToOrders(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
fun ChakulaLogo(modifier: Modifier = Modifier, title: String = "Chakula") {
    Row(modifier = modifier) {
        Text(text = title, fontWeight = FontWeight.Bold)
    }
}
