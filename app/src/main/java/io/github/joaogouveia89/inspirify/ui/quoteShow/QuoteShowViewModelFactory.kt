package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.joaogouveia89.inspirify.di.InspirifyComponent

class QuoteShowViewModelFactory(private val inspirifyComponent: InspirifyComponent) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuoteShowViewModel::class.java)) {
            return QuoteShowViewModel(inspirifyComponent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}