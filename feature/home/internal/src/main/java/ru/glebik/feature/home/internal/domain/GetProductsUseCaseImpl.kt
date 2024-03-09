package ru.glebik.feature.home.internal.domain

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.api.repository.ProductsRepository
import ru.glebik.feature.home.api.usecase.GetProductsUseCase

class GetProductsUseCaseImpl(
    private val repository: ProductsRepository,
) : GetProductsUseCase {
    override suspend fun invoke(skip: Int, limit: Int): ResultWrapper<List<Product>> {
        return repository.getProducts(skip, limit)
    }
}