package io.github.joaogouveia89.inspirify.di.modules

import dagger.Module
import dagger.Provides
import io.github.joaogouveia89.inspirify.dataSources.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.dataSources.local.LocalDb
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowRepository

@Module
class RepositoriesModule {

    @Provides
    fun provideQuoteShowRepository(api: RetrofitZenQuotes, localDb: LocalDb)
    = QuoteShowRepository(
        api = api,
        localDb = localDb
    )
}