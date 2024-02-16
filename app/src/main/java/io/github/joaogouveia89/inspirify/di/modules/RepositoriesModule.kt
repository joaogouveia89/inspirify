package io.github.joaogouveia89.inspirify.di.modules

import dagger.Module
import dagger.Provides
import io.github.joaogouveia89.inspirify.data.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.data.local.LocalDb
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteRepository

@Module
class RepositoriesModule {

    @Provides
    fun provideQuoteShowRepository(api: RetrofitZenQuotes, localDb: LocalDb) = QuoteRepository(
        api = api,
        localDb = localDb
    )
}