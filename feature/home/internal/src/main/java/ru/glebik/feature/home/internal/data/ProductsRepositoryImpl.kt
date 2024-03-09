package ru.glebik.feature.home.internal.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.api.repository.ProductsRepository
import ru.glebik.feature.home.api.service.ProductsService

internal class ProductsRepositoryImpl(
    private val productsService: ProductsService,
    private val ioDispatcher: CoroutineDispatcher,
) : ProductsRepository {

    override suspend fun getProducts(skip: Int, limit: Int): ResultWrapper<List<Product>> =
        withContext(ioDispatcher) {
            runCatching {
                val response = productsService.getProducts(skip, 20)
                cachedProducts = response.products
                response.products
            }.fold(
                onSuccess = {
                    ResultWrapper.Success(it)
                },
                onFailure = { ResultWrapper.Failed(it, it.message) }
            )
        }

    override suspend fun getProductById(id: Int): ResultWrapper<Product> =
        runCatching {
            Log.d("products", cachedProducts.toString())
            cachedProducts.first { it.id == id }
        }.fold(
            onSuccess = {
                ResultWrapper.Success(it)
            },
            onFailure = { ResultWrapper.Failed(it, it.message) }
        )

    override suspend fun searchProducts(q: String): ResultWrapper<List<Product>> =
        withContext(ioDispatcher) {
            runCatching {
                productsService.searchProduct(q).products
            }.fold(
                onSuccess = {
                    ResultWrapper.Success(it)
                },
                onFailure = { ResultWrapper.Failed(it, it.message) }
            )
        }

    override suspend fun getCategories(): List<String> {
        withContext(ioDispatcher) {
            categories = productsService.getCategories()
        }
        return categories
    }

    override suspend fun getProductsWithCategories(
        skip: Int,
        limit: Int,
        categories: List<String>
    ): ResultWrapper<List<Product>> = withContext(ioDispatcher) {
        runCatching {

            val resultList = mutableListOf<Product>()

            categories.forEachIndexed { index, category ->
                resultList.addAll(productsService.getProductsWithCategories(skip, 20, category).products)
            }

            cachedProducts = resultList
            resultList
        }.fold(
            onSuccess = {
                ResultWrapper.Success(it)
            },
            onFailure = { ResultWrapper.Failed(it, it.message) }
        )
    }

    companion object {
        private var cachedProducts = listOf<Product>()

        private var categories = listOf<String>()
    }
}