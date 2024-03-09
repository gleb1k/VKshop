package ru.glebik.feature.home.internal.domain

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.api.repository.ProductsRepository
import ru.glebik.feature.home.api.usecase.SearchProductsUseCase

class SearchProductsUseCaseImpl(
    private val repository: ProductsRepository,
) : SearchProductsUseCase {

    override suspend fun invoke(q: String): ResultWrapper<List<Product>> = repository.searchProducts(q)
}