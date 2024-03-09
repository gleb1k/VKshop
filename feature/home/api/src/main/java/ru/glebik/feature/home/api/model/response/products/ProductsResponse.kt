package ru.glebik.feature.home.api.model.response.products


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    @SerialName("products")
    val products: List<Product>,
    @SerialName("total")
    val total: Int?,
    @SerialName("skip")
    val skip: Int?,
    @SerialName("limit")
    val limit: Int?
)