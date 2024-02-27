package io.github.joaogouveia89.inspirify.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.joaogouveia89.inspirify.di.InspirifyComponent

class FavoritesViewModelFactory(private val inspirifyComponent: InspirifyComponent) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(inspirifyComponent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}