package ru.glebik.feature.home.api.usecase

import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product

interface GetProductByIdUseCase {
    suspend operator fun invoke(id: Int): ResultWrapper<Product>
}

