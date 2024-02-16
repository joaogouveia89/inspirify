package io.github.joaogouveia89.inspirify.ui.quoteShow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteFavoriteUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteShowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuotesInputs {
    val onRefresh = MutableLiveData<Unit>()
    val onFavoriteClick = MutableLiveData<Unit>()
}

interface QuotesOutputs {
    val quoteRequestStatus: LiveData<DataRequest>
}

interface QuoteViewModelType {
    val inputs: QuotesInputs
    val outputs: QuotesOutputs
}

class QuoteShowViewModel(inspirifyComponent: InspirifyComponent) : ViewModel(), QuoteViewModelType,
    QuotesOutputs {

    private val onFavoriteClickObserver = Observer<Unit> {
        quoteFavoriteUseCase
    }

    override val outputs = this
    override val inputs = QuotesInputs()

    init {
        inspirifyComponent.inject(this)
        inputs.onFavoriteClick.observeForever(onFavoriteClickObserver)
    }

    @Inject
    lateinit var quoteShowUseCase: QuoteShowUseCase

    @Inject
    lateinit var quoteFavoriteUseCase: QuoteFavoriteUseCase

    override val quoteRequestStatus: LiveData<DataRequest> = quoteShowUseCase.dataRequest

    fun fetchRandomQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            quoteShowUseCase.execute()
        }
    }

    override fun onCleared() {
        super.onCleared()
        inputs.onFavoriteClick.removeObserver(onFavoriteClickObserver)
    }
}