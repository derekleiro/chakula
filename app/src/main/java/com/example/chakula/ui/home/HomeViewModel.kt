package com.example.chakula.ui.home

import Order
import Product
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chakula.ChakulaApplication
import com.example.chakula.data.products.ProductsRepository
import com.example.jetnews.utils.ErrorMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface HomeUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    /**
     * There are no products to render.
     * This could either be because they are still loading or they failed to load, and we are
     * waiting to reload them.
     */
    data class NoProducts(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : HomeUiState


    data class HasProducts(
        val productsFeed: List<Product>,
        val selectedProducts: List<Product>,
        val orderedProducts: List<Order>,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : HomeUiState
}

private data class HomeViewModelState(
    val productsFeed: List<Product>? = null,
    val selectedProducts: List<Product> = emptyList(),
    val orderedProducts: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val searchInput: String = "",
) {

    fun toUiState(): HomeUiState =
        if (productsFeed == null) {
            HomeUiState.NoProducts(
                isLoading = isLoading,
                errorMessages = errorMessages,
            )
        } else {
            HomeUiState.HasProducts(
                productsFeed = productsFeed,
                selectedProducts = selectedProducts,
                orderedProducts = orderedProducts,
                isLoading = isLoading,
                errorMessages = errorMessages,
            )
        }
}

class HomeViewModel(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(
        HomeViewModelState(
            isLoading = true
        )
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        initialProductsFetch()
        getCartItems()
        getOrderedItems()
    }

    private fun initialProductsFetch() {
        val sharedPreferences = ChakulaApplication.getContext()?.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = productsRepository.getProducts()
            viewModelState.update {
                val gson = Gson()
                val json = gson.toJson(result.data)

                sharedPreferences?.edit()?.putString("home_products", json)?.apply()
                it.copy(productsFeed = result.data, isLoading = false)
            }
        }
    }

    /**
     * Refresh products and update the UI state accordingly
     */
    fun refreshProducts() {
        val sharedPreferences = ChakulaApplication.getContext()?.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)

        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = productsRepository.refreshProducts()
            viewModelState.update {
                val gson = Gson()
                val json = gson.toJson(result.data)

                sharedPreferences?.edit()?.putString("home_products", json)?.apply()
                it.copy(productsFeed = result.data, isLoading = false)
            }
        }
    }

    private fun getCartItems() {
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        val sharedPreferences = ChakulaApplication.getContext()?.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)

        val items = sharedPreferences?.getString("cart_items", emptyList<Product>().toString())
        val gson = Gson()

        // Convert the JSON string to a List<Product> object
        val productListType = object : TypeToken<List<Product>>() {}.type
        val productList: List<Product> = gson.fromJson(items, productListType)

        if(productList.isNotEmpty()){
            viewModelScope.launch {
                viewModelState.update {
                    it.copy(selectedProducts = productList, isLoading = false)
                }
            }
        }
    }

    private fun getOrderedItems() {
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        val sharedPreferences = ChakulaApplication.getContext()?.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)

        val items = sharedPreferences?.getString("orders", emptyList<Product>().toString())
        val gson = Gson()

        // Convert the JSON string to a List<Product> object
        val ordersListType = object : TypeToken<List<Order>>() {}.type
        val ordersList: List<Order> = gson.fromJson(items, ordersListType)

        if(ordersList.isNotEmpty()){
            viewModelScope.launch {
                viewModelState.update {
                    it.copy(orderedProducts = ordersList, isLoading = false)
                }
            }
        }
    }

    fun addToCart(newProduct: Product, sharedPreferences: SharedPreferences) {
        val items = viewModelState.value.selectedProducts.plus(newProduct)
        val gson = Gson()
        val json = gson.toJson(items)

        sharedPreferences.edit().putString("cart_items", json).apply()
    }

    // TODO: To be implemented
    fun removeFromCart(newProduct: Product, sharedPreferences: SharedPreferences) {

    }

    fun placeOrder(sharedPreferences: SharedPreferences) {
        val items = sharedPreferences.getString("orders", emptyList<Product>().toString())
        val gson = Gson()

        // Convert the JSON string to a List<Order> object
        val ordersListType = object : TypeToken<List<Order>>() {}.type
        val ordersList: List<Order> = gson.fromJson(items, ordersListType)

        val totalPrice = viewModelState.value.selectedProducts.sumOf { it.price }
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)

        val order = Order(formattedDate, totalPrice, viewModelState.value.selectedProducts)
        val orders = ordersList.plus(order)

        val json = gson.toJson(orders)

        sharedPreferences.edit().putString("orders", json).apply()
        sharedPreferences.edit().remove("cart_items").apply()

        viewModelScope.launch {
            viewModelState.update {
                it.copy(selectedProducts = emptyList(), isLoading = false)
            }
        }
    }

    /**
     * Notify the user that an error has occurred, on screen
     */
    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }


    /**
     * Factory for HomeViewModel that takes PostsRepository as a dependency
     */
    companion object {
        fun provideFactory(
            productsRepository: ProductsRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(productsRepository) as T
            }
        }
    }
}
