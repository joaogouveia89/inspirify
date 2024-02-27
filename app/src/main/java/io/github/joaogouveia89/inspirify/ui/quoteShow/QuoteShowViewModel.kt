package io.github.joaogouveia89.inspirify.ui.quoteShow

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
    val requestNewData = MutableLiveData<Unit>()
    val onFavoriteClick = MutableLiveData<Unit>()
}

interface QuotesOutputs {
    val quoteRequestStatus: LiveData<DataRequest>
    val quoteFavoriteUpdateStatus: LiveData<DataRequest>
}

interface QuoteViewModelType {
    val inputs: QuotesInputs
    val outputs: QuotesOutputs
}

class QuoteShowViewModel(inspirifyComponent: InspirifyComponent) : ViewModel(), QuoteViewModelType,
    QuotesOutputs {

    private val onFavoriteClickObserver = Observer<Unit> {
        quoteShowUseCase.dataRequest.value?.let {
            if (it is DataRequest.Success<*>) {
                val currentQuote = it.data as? Quote
                if (currentQuote != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        quoteFavoriteUseCase.execute(currentQuote)
                    }
                }
            }
        }
    }

    private val requestNewDataObserver = Observer<Unit> {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRandomQuote()
        }
    }

    override val outputs = this
    override val inputs = QuotesInputs()

    init {
        inspirifyComponent.inject(this)
        inputs.onFavoriteClick.observeForever(onFavoriteClickObserver)
        inputs.requestNewData.observeForever(requestNewDataObserver)
    }

    @Inject
    lateinit var quoteShowUseCase: QuoteShowUseCase

    @Inject
    lateinit var quoteFavoriteUseCase: QuoteFavoriteUseCase

    override val quoteRequestStatus: LiveData<DataRequest> = quoteShowUseCase.dataRequest

    override val quoteFavoriteUpdateStatus: LiveData<DataRequest> = quoteFavoriteUseCase.dataRequest

    suspend private fun fetchRandomQuote() {
        quoteShowUseCase.execute()
    }

    override fun onCleared() {
        super.onCleared()
        inputs.onFavoriteClick.removeObserver(onFavoriteClickObserver)
        inputs.requestNewData.removeObserver(requestNewDataObserver)
    }
}