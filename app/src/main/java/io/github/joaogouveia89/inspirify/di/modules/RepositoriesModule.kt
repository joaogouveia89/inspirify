package io.github.joaogouveia89.inspirify.di.modules

import dagger.Module
import dagger.Provides
import io.github.joaogouveia89.inspirify.data.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.data.local.LocalDb
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowRepository

@Module
class RepositoriesModule {

    @Provides
    fun provideQuoteShowRepository(api: RetrofitZenQuotes, localDb: LocalDb) = QuoteShowRepository(
        api = api,
        localDb = localDb
    )
}