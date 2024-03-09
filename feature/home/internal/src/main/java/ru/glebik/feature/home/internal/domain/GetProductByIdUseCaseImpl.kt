package ru.glebik.feature.home.internal.domain

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.api.repository.ProductsRepository
import ru.glebik.feature.home.api.usecase.GetProductByIdUseCase
import ru.glebik.feature.home.api.usecase.GetProductsUseCase

class GetProductByIdUseCaseImpl(
    private val repository: ProductsRepository,
) : GetProductByIdUseCase {
    override suspend fun invoke(id: Int): ResultWrapper<Product> = repository.getProductById(id)
}
