package ru.glebik.feature.home.api.service

import ru.glebik.feature.home.api.model.response.products.ProductsResponse

interface ProductsService {

    suspend fun getProducts(skip: Int, limit: Int): ProductsResponse

    suspend fun searchProduct(q: String): ProductsResponse

    suspend fun getCategories() : List<String>

    suspend fun getProductsWithCategories(skip: Int, limit: Int, category: String): ProductsResponse
}