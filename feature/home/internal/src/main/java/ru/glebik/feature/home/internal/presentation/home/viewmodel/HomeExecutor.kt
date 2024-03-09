package ru.glebik.feature.home.internal.presentation.home.viewmodel

import kotlinx.collections.immutable.toPersistentList
import ru.glebik.core.presentation.BaseExecutor
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.usecase.GetCategoriesUseCase
import ru.glebik.feature.home.api.usecase.GetProductsUseCase
import ru.glebik.feature.home.api.usecase.GetProductsWithCategoriesUseCase
import ru.glebik.feature.home.api.usecase.SearchProductsUseCase

internal class HomeExecutor(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getProductsWithCategoriesUseCase: GetProductsWithCategoriesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseExecutor<HomeStore.Intent, Nothing, HomeStore.State, HomeStoreFactory.Message, Nothing>() {

    override suspend fun suspendExecuteIntent(
        intent: HomeStore.Intent,
        getState: () -> HomeStore.State,
    ) = when (intent) {
        is HomeStore.Intent.LoadProducts -> loadProducts(intent.page, intent.categories)
        is HomeStore.Intent.EnableSearch -> dispatch(HomeStoreFactory.Message.EnableSearch(intent.isSearchEnable))
        is HomeStore.Intent.OnQuerySearchChange -> dispatch(
            HomeStoreFactory.Message.SetQuerySearch(
                intent.query
            )
        )

        is HomeStore.Intent.Search -> search(intent.query)
        is HomeStore.Intent.ChangeCategories -> dispatch(HomeStoreFactory.Message.SetCategories(intent.categories))
        HomeStore.Intent.LoadCategories -> loadCategories()
    }

    private suspend fun loadProducts(page: Int, categories: List<CategoryUi>) {
        dispatch(HomeStoreFactory.Message.SetLoading)
        val skip = 20 * (page - 1)

        val filtered = categories.filter { it.isEnabled }
        val response = if (filtered.isEmpty()) {
            getProductsUseCase(skip)
        } else {
            val categoriesString = filtered.map { it.title }
            getProductsWithCategoriesUseCase(skip, categories = categoriesString)
        }
        when (response) {
            is ResultWrapper.Success -> {
                dispatch(
                    HomeStoreFactory.Message.SetProductsList(response.data.toPersistentList())
                )
                dispatch(
                    HomeStoreFactory.Message.SetCurrentPage(page)
                )
            }

            is ResultWrapper.Failed -> dispatch(
                HomeStoreFactory.Message.SetError(
                    ResultWrapper.Failed(
                        response.exception,
                        response.errorMessage
                    )
                )
            )
        }
    }

    private suspend fun search(query: String) {
        dispatch(HomeStoreFactory.Message.SetLoading)
        when (val response = searchProductsUseCase(query)) {
            is ResultWrapper.Success -> {

                if (response.data.isEmpty())
                    dispatch(HomeStoreFactory.Message.SetNotFound)
                else
                    dispatch(HomeStoreFactory.Message.SetProductsList(response.data.toPersistentList()))
            }

            is ResultWrapper.Failed -> HomeStoreFactory.Message.SetError(
                ResultWrapper.Failed(
                    response.exception,
                    response.errorMessage
                )
            )
        }
    }

    private suspend fun loadCategories() {
        val response = getCategoriesUseCase()

        val categories = response.map { CategoryUi(it, false) }
        dispatch(HomeStoreFactory.Message.SetCategories(categories))
    }
}
