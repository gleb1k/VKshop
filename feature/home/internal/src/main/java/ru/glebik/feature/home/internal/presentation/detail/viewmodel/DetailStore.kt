package ru.glebik.feature.home.internal.presentation.detail.viewmodel

import com.arkivanov.mvikotlin.core.store.Store
import ru.glebik.feature.home.api.model.response.products.Product

interface DetailStore : Store<DetailStore.Intent, DetailStore.State, Nothing> {

    data class State internal constructor(
        val product: Product? = null,

        val isLoading: Boolean = false,
        val isError: Boolean = false,
    )

    sealed interface Intent {
        data class Load(val id: Int) : Intent
    }
}


