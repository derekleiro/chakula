package com.example.chakula.data.products

import PaymentResponse
import ProductResponse
import retrofit2.http.GET

// Products and Payments data layer
interface ProductsRepository {

    // Get the list of products
    @GET("api/test/products")
    suspend fun getProducts(): ProductResponse
    suspend fun refreshProducts(): ProductResponse

    // Get the list of payments
    @GET("api/test/payment-types")
    suspend fun getPayments(): PaymentResponse

}
