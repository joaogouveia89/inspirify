package io.github.joaogouveia89.inspirify.di.modules

import dagger.Module
import dagger.Provides
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteRepository
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteFavoriteUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteShowUseCase

@Module
class UseCasesModule {
    @Provides
    fun provideQuoteShowUseCase(repository: QuoteRepository) = QuoteShowUseCase(repository)

    @Provides
    fun provideQuoteFavoriteUseCase(repository: QuoteRepository) =
        QuoteFavoriteUseCase(repository)
}