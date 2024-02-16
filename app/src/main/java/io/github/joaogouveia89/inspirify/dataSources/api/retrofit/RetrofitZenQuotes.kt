package io.github.joaogouveia89.inspirify.dataSources.api.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//https://docs.zenquotes.io/zenquotes-documentation/
const val FETCH_RANDOM_QUOTE_URL = "random/"
const val BASE_URL = "https://zenquotes.io/api/"

class RetrofitZenQuotes {
    private  val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: RetrofitService = retrofit.create(RetrofitService::class.java)
    suspend fun fetchRandomQuote() = service.fetchRandomQuote()
}