package com.moragar.catbreeds.catbreed.data.remote

import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.core.util.Constants.ENDPOINT
import com.moragar.catbreeds.core.util.Constants.LIMIT
import com.moragar.catbreeds.core.util.Constants.PAGE
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface CatApi {
    suspend fun getBreeds(limit: Int, page: Int): List<CatBreed>
}

class CatApiImpl(private val client: HttpClient) : CatApi {
    override suspend fun getBreeds(limit: Int, page: Int): List<CatBreed> {
        return client.get(ENDPOINT) {
            parameter(LIMIT, limit)
            parameter(PAGE, page)
        }.body()
    }
}