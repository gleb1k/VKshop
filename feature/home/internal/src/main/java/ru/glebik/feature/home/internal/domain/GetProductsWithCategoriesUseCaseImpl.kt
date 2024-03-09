package ru.glebik.feature.home.internal.domain

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.api.repository.ProductsRepository
import ru.glebik.feature.home.api.usecase.GetProductsWithCategoriesUseCase

class GetProductsWithCategoriesUseCaseImpl(
    private val repository: ProductsRepository,
) : GetProductsWithCategoriesUseCase {
    override suspend fun invoke(
        skip: Int,
        limit: Int,
        categories: List<String>
    ): ResultWrapper<List<Product>> {
        return repository.getProductsWithCategories(skip, limit, categories)
    }
}