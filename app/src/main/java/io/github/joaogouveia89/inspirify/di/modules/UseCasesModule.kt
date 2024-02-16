package io.github.joaogouveia89.inspirify.di.modules

import dagger.Module
import dagger.Provides
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowRepository
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteFavoriteUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteShowUseCase

@Module
class UseCasesModule {
    @Provides
    fun provideQuoteShowUseCase(repository: QuoteShowRepository) = QuoteShowUseCase(repository)

    @Provides
    fun provideQuoteFavoriteUseCase(repository: QuoteShowRepository) =
        QuoteFavoriteUseCase(repository)
}