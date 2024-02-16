package io.github.joaogouveia89.inspirify.ui.quoteShow

import io.github.joaogouveia89.inspirify.dataSources.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.dataSources.local.LocalDb
import javax.inject.Inject

class QuoteShowRepository @Inject constructor(
    private val api: RetrofitZenQuotes,
    private val localDb: LocalDb
)  {
}