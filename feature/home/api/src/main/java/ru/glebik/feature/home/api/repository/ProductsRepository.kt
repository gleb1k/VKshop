package ru.glebik.feature.home.api.repository

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.api.model.response.products.ProductsResponse

interface ProductsRepository {

    suspend fun getProducts(skip: Int, limit: Int = 20): ResultWrapper<List<Product>>

    suspend fun getProductById(id: Int): ResultWrapper<Product>

    suspend fun searchProducts(q: String): ResultWrapper<List<Product>>

    suspend fun getCategories() : List<String>

    suspend fun getProductsWithCategories(skip: Int, limit: Int, categories: List<String>): ResultWrapper<List<Product>>

}