package ru.glebik.feature.home.internal.presentation.home.viewmodel

import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.glebik.feature.home.api.model.response.products.Product

interface HomeStore : Store<HomeStore.Intent, HomeStore.State, Nothing> {

    data class State internal constructor(
        val products: PersistentList<Product> = persistentListOf(),
        val isSearchEnable: Boolean = false,
        val currentPage: Int = 1,

        val categories : List<CategoryUi> = listOf(),
        val querySearch: String = "",

        val isNotFound: Boolean = false,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
    )

    sealed interface Intent {
        data class LoadProducts(val page: Int, val categories: List<CategoryUi>) : Intent

        data object LoadCategories: Intent
        data class ChangeCategories(val categories: List<CategoryUi>) : Intent
        data class EnableSearch(val isSearchEnable: Boolean) : Intent


        data class OnQuerySearchChange(val query: String) : Intent
        data class Search(val query: String) : Intent
    }
}

data class CategoryUi(
    val title : String,
    //плохо
    var isEnabled: Boolean
)


