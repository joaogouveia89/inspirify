package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.CheckFavoriteStateUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteAddToFavoriteUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteShowUseCase
import javax.inject.Inject

class QuoteShowViewModelFactory(inspirifyComponent: InspirifyComponent) :
    ViewModelProvider.Factory {

        init {
            inspirifyComponent.inject(this)
        }

    @Inject
    lateinit var quoteShowUseCase: QuoteShowUseCase
    @Inject
    lateinit var quoteAddToFavoriteUseCase: QuoteAddToFavoriteUseCase
    @Inject
    lateinit var checkFavoriteStateUseCase: CheckFavoriteStateUseCase

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuoteShowViewModel::class.java)) {
            return QuoteShowViewModel(
                quoteShowUseCase = quoteShowUseCase,
                quoteAddToFavoriteUseCase = quoteAddToFavoriteUseCase,
                checkFavoriteStateUseCase = checkFavoriteStateUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}