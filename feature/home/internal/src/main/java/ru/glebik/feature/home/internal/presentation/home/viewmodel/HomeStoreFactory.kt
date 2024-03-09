package ru.glebik.feature.home.internal.presentation.home.viewmodel

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.collections.immutable.PersistentList
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.api.usecase.GetCategoriesUseCase
import ru.glebik.feature.home.api.usecase.GetProductsUseCase
import ru.glebik.feature.home.api.usecase.GetProductsWithCategoriesUseCase
import ru.glebik.feature.home.api.usecase.SearchProductsUseCase

internal class HomeStoreFactory(
    private val storeFactory: StoreFactory,

    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getProductsWithCategoriesUseCase: GetProductsWithCategoriesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) {
    fun create(): HomeStore = object :
        HomeStore,
        Store<HomeStore.Intent, HomeStore.State, Nothing> by storeFactory.create(
            name = HomeStore::class.simpleName,
            initialState = HomeStore.State(),
            bootstrapper = null,
            executorFactory = {
                HomeExecutor(
                    getProductsUseCase,
                    searchProductsUseCase,
                    getProductsWithCategoriesUseCase,
                    getCategoriesUseCase
                )
            },
            reducer = HomeReducer(),
        ) {}

    sealed interface Message {

        data class SetProductsList(
            val list: PersistentList<Product>
        ) : Message

        data class EnableSearch(
            val isSearchEnable: Boolean
        ) : Message

        data class SetCurrentPage(
            val page: Int
        ) : Message

        data class SetQuerySearch(val query: String) : Message
        data class SetCategories(val categories: List<CategoryUi>) : Message


        data object SetNotFound : Message
        data object SetLoading : Message
        data class SetError(val error: ResultWrapper.Failed) : Message
    }
}