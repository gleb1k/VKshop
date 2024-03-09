package ru.glebik.feature.home.internal.presentation.detail.viewmodel

import ru.glebik.core.presentation.BaseExecutor
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.feature.home.api.usecase.GetProductByIdUseCase

internal class DetailExecutor(
    private val getProductByIdUseCase: GetProductByIdUseCase
) : BaseExecutor<DetailStore.Intent, Nothing, DetailStore.State, DetailStoreFactory.Message, Nothing>() {

    override suspend fun suspendExecuteIntent(
        intent: DetailStore.Intent,
        getState: () -> DetailStore.State,
    ) = when (intent) {
        is DetailStore.Intent.Load -> load(intent.id)
    }

    private suspend fun load(id: Int) {
        dispatch(DetailStoreFactory.Message.SetLoading)
        when (val response = getProductByIdUseCase(id)) {
            is ResultWrapper.Success -> {
                dispatch(
                    DetailStoreFactory.Message.SetProduct(response.data)
                )
            }

            is ResultWrapper.Failed -> dispatch(
                DetailStoreFactory.Message.SetError(
                    ResultWrapper.Failed(
                        response.exception,
                        response.errorMessage
                    )
                )
            )
        }
    }

}
