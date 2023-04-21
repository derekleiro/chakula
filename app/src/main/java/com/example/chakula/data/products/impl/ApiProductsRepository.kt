package com.example.chakula.data.products.impl

import PaymentResponse
import Product
import ProductResponse
import android.content.Context
import com.example.chakula.ChakulaApplication
import com.example.chakula.data.products.ProductsRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.Moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ApiProductsRepository(context: Context) : ProductsRepository {
    // Define a Moshi instance for JSON serialization and deserialization

    private val moshi: Moshi = Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.drift.co.ke/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    private val api = retrofit.create(ProductsRepository::class.java)


    // Used to make suspend functions that read and update state safe to call from any thread
    override suspend fun getProducts(): ProductResponse {

        // Checking if products already exist before calling the API
        val items =
            ChakulaApplication.getContext()?.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)
                ?.getString("home_products", emptyList<Product>().toString())

        val gson = Gson()

        // Convert the JSON string to a List<Product> object
        val productListType = object : TypeToken<List<Product>>() {}.type
        val productList: List<Product> = gson.fromJson(items, productListType)

        return if(productList.isEmpty()){
            api.getProducts()
        }else{
            ProductResponse("409", "Products already exists", productList)
        }
    }

    // Used to make suspend functions that read and update state safe to call from any thread
    override suspend fun refreshProducts(): ProductResponse {
        return api.getProducts()
    }

    override suspend fun getPayments(): PaymentResponse {
        return api.getPayments()
    }
}
