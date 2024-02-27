package io.github.joaogouveia89.inspirify.di

import dagger.Component
import io.github.joaogouveia89.inspirify.di.modules.DataModule
import io.github.joaogouveia89.inspirify.di.modules.RepositoriesModule
import io.github.joaogouveia89.inspirify.di.modules.UseCasesModule
import io.github.joaogouveia89.inspirify.ui.favorites.FavoritesViewModel
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, RepositoriesModule::class, UseCasesModule::class])
interface InspirifyComponent {
    fun inject(viewModel: QuoteShowViewModel)
    fun inject(viewModel: FavoritesViewModel)
}