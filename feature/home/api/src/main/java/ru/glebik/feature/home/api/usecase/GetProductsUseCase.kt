package ru.glebik.feature.home.api.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product

interface GetProductsUseCase {
    suspend operator fun invoke(skip : Int, limit: Int = 20): ResultWrapper<List<Product>>
}