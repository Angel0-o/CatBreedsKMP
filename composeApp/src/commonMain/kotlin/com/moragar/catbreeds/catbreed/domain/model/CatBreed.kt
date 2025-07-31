package com.moragar.catbreeds.catbreed.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatBreed(
    @SerialName("id") val id: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("origin") val origin: String = "",
    @SerialName("temperament") val temperament: String = "",
    @SerialName("life_span") val lifeSpan: String = "",
    @SerialName("image") val image: CatImage? = null,
    var isFavorite: Boolean = false
)

@Serializable
data class CatImage(
    val url: String
)