package ru.glebik.feature.home.internal.presentation.home.viewmodel

import ru.glebik.core.presentation.MviScreenModel


class HomeScreenModel(
    private val store: HomeStore,
) : MviScreenModel<HomeStore.Intent, HomeStore.State, Nothing>(
    store
) {

    init {
        loadProducts(1)
        loadCategories()
    }

    fun loadProducts(page: Int) {
        if (page <= 0)
            return
        store.accept(HomeStore.Intent.LoadProducts(page, state.value.categories))
    }

    private fun loadCategories() {
        store.accept(HomeStore.Intent.LoadCategories)
    }

    fun onChangeCategory(categoryUi: CategoryUi) {
        val categories =
            state.value.categories.map { if (it.title == categoryUi.title) categoryUi else it }
        store.accept(HomeStore.Intent.ChangeCategories(categories))
    }

    fun enableSearch(isSearchEnable: Boolean) =
        store.accept(HomeStore.Intent.EnableSearch(isSearchEnable))

    fun onQuerySearchChange(query: String) =
        store.accept(HomeStore.Intent.OnQuerySearchChange(query))

    fun onSearchClick(query: String) {
        store.accept(HomeStore.Intent.Search(query = query))
    }

    fun isCategoryEnabled(): Boolean {
        return state.value.categories.find { it.isEnabled } != null
    }
}
