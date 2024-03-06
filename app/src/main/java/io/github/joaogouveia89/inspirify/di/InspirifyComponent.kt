package io.github.joaogouveia89.inspirify.di

import dagger.Component
import io.github.joaogouveia89.inspirify.di.modules.DataModule
import io.github.joaogouveia89.inspirify.di.modules.RepositoriesModule
import io.github.joaogouveia89.inspirify.di.modules.UseCasesModule
import io.github.joaogouveia89.inspirify.ui.favorites.FavoritesViewModel
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowViewModel
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowViewModelFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, RepositoriesModule::class, UseCasesModule::class])
interface InspirifyComponent {
    fun inject(factory: QuoteShowViewModelFactory)
    fun inject(viewModel: FavoritesViewModel)
}