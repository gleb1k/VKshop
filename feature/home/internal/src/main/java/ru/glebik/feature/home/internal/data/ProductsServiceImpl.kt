package ru.glebik.feature.home.internal.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import ru.glebik.feature.home.api.model.response.products.ProductsResponse
import ru.glebik.feature.home.api.service.ProductsService

internal class ProductsServiceImpl(
    private val client: HttpClient
) : ProductsService {

    override suspend fun getProducts(skip: Int, limit: Int): ProductsResponse =
        client.get {
            url {
                path("/products")
                parameter("skip", skip)
                parameter("limit", limit)
            }
        }.body()

    override suspend fun searchProduct(q: String): ProductsResponse =
        client.get {
            url {
                path("/products/search")
                parameter("q", q)
            }
        }.body()

    override suspend fun getCategories(): List<String> =
        client.get {
            url {
                path("/products/categories")
            }
        }.body()

    override suspend fun getProductsWithCategories(
        skip: Int,
        limit: Int,
        category: String
    ): ProductsResponse =
        client.get {
            url {
                path("/products/category/${category}")
                parameter("skip", skip)
                parameter("limit", limit)
            }
        }.body()
}