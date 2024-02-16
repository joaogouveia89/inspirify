package io.github.joaogouveia89.inspirify.dataSources.api.retrofit

import io.github.joaogouveia89.inspirify.dataSources.api.QuoteApi
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService {
    @GET(FETCH_RANDOM_QUOTE_URL)
    suspend fun fetchRandomQuote(): Response<QuoteApi>
}