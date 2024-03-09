package ru.glebik.feature.home.api.usecase

interface GetCategoriesUseCase {
    suspend operator fun invoke(): List<String>
}