package io.github.joaogouveia89.inspirify.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.github.joaogouveia89.inspirify.ui.favorites.useCases.DeleteFavoritesUseCase
import io.github.joaogouveia89.inspirify.ui.favorites.useCases.FetchFavoritesUseCase
import javax.inject.Inject

class FavoritesViewModelFactory(inspirifyComponent: InspirifyComponent) :
    ViewModelProvider.Factory {

        init {
            inspirifyComponent.inject(this)
        }

    @Inject
    lateinit var fetchFavoritesUseCase: FetchFavoritesUseCase
    @Inject
    lateinit var deleteFavoritesUseCase: DeleteFavoritesUseCase

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(
                fetchFavoritesUseCase = fetchFavoritesUseCase,
                deleteFavoritesUseCase = deleteFavoritesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}