package ru.glebik.feature.home.internal.presentation.detail.viewmodel

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.model.response.products.Product
import ru.glebik.feature.home.api.usecase.GetProductByIdUseCase

internal class DetailStoreFactory(
    private val storeFactory: StoreFactory,
    private val getProductByIdUseCase: GetProductByIdUseCase,
) {
    fun create(): DetailStore = object :
        DetailStore,
        Store<DetailStore.Intent, DetailStore.State, Nothing> by storeFactory.create(
            name = DetailStore::class.simpleName,
            initialState = DetailStore.State(),
            bootstrapper = null,
            executorFactory = {
                DetailExecutor(
                    getProductByIdUseCase
                )
            },
            reducer = DetailReducer(),
        ) {}

    sealed interface Message {
        data object SetLoading : Message
        data class SetProduct(
            val product: Product
        ) : Message

        data class SetError(val error: ResultWrapper.Failed) : Message
    }
}