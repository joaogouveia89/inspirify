package io.github.joaogouveia89.inspirify.di.modules

import dagger.Module
import dagger.Provides
import io.github.joaogouveia89.inspirify.data.repositories.QuoteRepository
import io.github.joaogouveia89.inspirify.ui.favorites.useCases.FetchFavoritesUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteAddToFavoriteUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteShowUseCase

@Module
class UseCasesModule {
    @Provides
    fun provideQuoteShowUseCase(repository: QuoteRepository) = QuoteShowUseCase(repository)

    @Provides
    fun provideQuoteFavoriteUseCase(repository: QuoteRepository) =
        QuoteAddToFavoriteUseCase(repository)

    @Provides
    fun provideFetchFavoritesUseCase(repository: QuoteRepository) =
        FetchFavoritesUseCase(repository)
}