package ru.glebik.feature.home.api.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product

interface SearchProductsUseCase {
    suspend operator fun invoke(q: String): ResultWrapper<List<Product>>
}