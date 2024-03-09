package ru.glebik.feature.home.internal.domain

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.api.repository.ProductsRepository
import ru.glebik.feature.home.api.usecase.GetCategoriesUseCase
import ru.glebik.feature.home.api.usecase.GetProductsUseCase

class GetCategoriesUseCaseImpl(
    private val repository: ProductsRepository,
) : GetCategoriesUseCase {
    override suspend fun invoke(): List<String> {
        return repository.getCategories()
    }
}