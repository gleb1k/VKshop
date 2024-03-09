package ru.glebik.feature.home.internal.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.glebik.core.navigation.SharedScreen
import ru.glebik.core.utils.di.CoroutineDispatchers
import ru.glebik.feature.home.api.repository.ProductsRepository
import ru.glebik.feature.home.api.service.ProductsService
import ru.glebik.feature.home.api.usecase.GetCategoriesUseCase
import ru.glebik.feature.home.api.usecase.GetProductByIdUseCase
import ru.glebik.feature.home.api.usecase.GetProductsUseCase
import ru.glebik.feature.home.api.usecase.GetProductsWithCategoriesUseCase
import ru.glebik.feature.home.api.usecase.SearchProductsUseCase
import ru.glebik.feature.home.internal.data.ProductsRepositoryImpl
import ru.glebik.feature.home.internal.data.ProductsServiceImpl
import ru.glebik.feature.home.internal.domain.GetCategoriesUseCaseImpl
import ru.glebik.feature.home.internal.domain.GetProductByIdUseCaseImpl
import ru.glebik.feature.home.internal.domain.GetProductsUseCaseImpl
import ru.glebik.feature.home.internal.domain.GetProductsWithCategoriesUseCaseImpl
import ru.glebik.feature.home.internal.domain.SearchProductsUseCaseImpl
import ru.glebik.feature.home.internal.presentation.detail.DetailScreen
import ru.glebik.feature.home.internal.presentation.detail.viewmodel.DetailScreenModel
import ru.glebik.feature.home.internal.presentation.detail.viewmodel.DetailStore
import ru.glebik.feature.home.internal.presentation.detail.viewmodel.DetailStoreFactory
import ru.glebik.feature.home.internal.presentation.home.HomeScreen
import ru.glebik.feature.home.internal.presentation.home.viewmodel.HomeScreenModel
import ru.glebik.feature.home.internal.presentation.home.viewmodel.HomeStore
import ru.glebik.feature.home.internal.presentation.home.viewmodel.HomeStoreFactory

val homeModule = module {
    single<ProductsService> {
        ProductsServiceImpl(get())
    }
    factory<ProductsRepository> {
        ProductsRepositoryImpl(
            productsService = get(),
            ioDispatcher = get(named(CoroutineDispatchers.IO)),
        )
    }

    factory<HomeStore> {
        HomeStoreFactory(
            storeFactory = get(),
            getProductsUseCase = get(),
            searchProductsUseCase = get(),
            get(),
            get()
        ).create()
    }

    factory<HomeScreenModel> { HomeScreenModel(get()) }

    factory<GetProductsUseCase> {
        GetProductsUseCaseImpl(get())
    }

    factory<GetCategoriesUseCase> {
        GetCategoriesUseCaseImpl(get())
    }

    factory<SearchProductsUseCase> {
        SearchProductsUseCaseImpl(get())
    }

    factory<GetProductsWithCategoriesUseCase> {
        GetProductsWithCategoriesUseCaseImpl(get())
    }

    //--detail
    factory<DetailStore> {
        DetailStoreFactory(
            storeFactory = get(), getProductByIdUseCase =get(), 
        ).create()
    }

    factory<GetProductByIdUseCase> {
        GetProductByIdUseCaseImpl(get())
    }

    factory<DetailScreenModel> { DetailScreenModel(get()) }

}

val homeScreenModule = screenModule {
    register<SharedScreen.Home> { HomeScreen }
    register<SharedScreen.Detail> { DetailScreen(it.id) }
}