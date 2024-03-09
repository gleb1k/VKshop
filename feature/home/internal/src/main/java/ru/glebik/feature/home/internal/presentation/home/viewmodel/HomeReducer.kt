package ru.glebik.feature.home.internal.presentation.home.viewmodel

import com.arkivanov.mvikotlin.core.store.Reducer

internal class HomeReducer : Reducer<HomeStore.State, HomeStoreFactory.Message> {

    override fun HomeStore.State.reduce(
        msg: HomeStoreFactory.Message,
    ) = when (msg) {
        is HomeStoreFactory.Message.SetError -> copy(
            isError = true,
            isLoading = false,
            isNotFound = false,
        )

        is HomeStoreFactory.Message.SetLoading -> copy(
            isLoading = true,
            isError = false,
            isNotFound = false,
        )

        is HomeStoreFactory.Message.SetProductsList -> copy(
            isNotFound = false,
            isLoading = false,
            isError = false,
            products = msg.list
        )


        is HomeStoreFactory.Message.EnableSearch -> copy(
            isSearchEnable = msg.isSearchEnable
        )

        is HomeStoreFactory.Message.SetQuerySearch -> copy(
            querySearch = msg.query
        )

        HomeStoreFactory.Message.SetNotFound -> copy(
            isNotFound = true,
            isLoading = false,
            isError = false,
        )

        is HomeStoreFactory.Message.SetCurrentPage -> copy(
            currentPage = msg.page
        )

        is HomeStoreFactory.Message.SetCategories -> copy(
            categories = msg.categories
        )

    }
}