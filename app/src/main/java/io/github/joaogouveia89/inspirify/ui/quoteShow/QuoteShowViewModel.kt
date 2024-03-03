package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteAddToFavoriteUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteShowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuotesInputs {
    val requestNewData = MutableLiveData<Unit>()
    val onFavoriteClick = MutableLiveData<Unit>()
    val updateQuote = MutableLiveData<Quote>()
}

interface QuotesOutputs {
    val currentQuote: LiveData<Quote>
    val showLoading: LiveData<Boolean>
    val showError: LiveData<String>
}

interface QuoteViewModelType {
    val inputs: QuotesInputs
    val outputs: QuotesOutputs
}

class QuoteShowViewModel(inspirifyComponent: InspirifyComponent) : ViewModel(), QuoteViewModelType,
    QuotesOutputs {

    private val onFavoriteClickObserver = Observer<Unit> {
        outputs.currentQuote.value?.let { quote ->
            viewModelScope.launch(Dispatchers.IO) {
                quoteAddToFavoriteUseCase.execute(quote)
            }
        }
    }

    private val requestNewDataObserver = Observer<Unit> {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRandomQuote()
        }
    }

    private val updateQuoteObserver = Observer<Quote> {
        _currentQuote.postValue(it)
    }

    private val onDataRequestObserver = Observer<DataRequest> { response ->
        when (response) {
            is DataRequest.OnProgress -> {
                _showLoading.postValue(true)
            }

            is DataRequest.Success<*> -> {
                _showLoading.postValue(false)
                val quote = response.data as? Quote
                quote?.let {
                    _currentQuote.postValue(it)
                }
            }

            is DataRequest.Failed -> {
                _showLoading.postValue(false)
                _showError.postValue(response.errorMessage)
            }
        }
    }

    override val outputs = this
    override val inputs = QuotesInputs()

    init {
        inspirifyComponent.inject(this)
        inputs.onFavoriteClick.observeForever(onFavoriteClickObserver)
        inputs.requestNewData.observeForever(requestNewDataObserver)
        inputs.updateQuote.observeForever(updateQuoteObserver)
        quoteAddToFavoriteUseCase.dataRequest.observeForever(onDataRequestObserver)
        quoteShowUseCase.dataRequest.observeForever(onDataRequestObserver)
    }

    @Inject
    lateinit var quoteShowUseCase: QuoteShowUseCase

    @Inject
    lateinit var quoteAddToFavoriteUseCase: QuoteAddToFavoriteUseCase

    private val _currentQuote = MutableLiveData<Quote>()
    private val _showLoading = MutableLiveData<Boolean>()
    private val _showError = MutableLiveData<String>()

    override val currentQuote: LiveData<Quote>
        get() = _currentQuote

    override val showLoading: LiveData<Boolean>
        get() = _showLoading
    override val showError: LiveData<String>
        get() = _showError


    private suspend fun fetchRandomQuote() {
        quoteShowUseCase.execute()
    }

    override fun onCleared() {
        super.onCleared()
        inputs.onFavoriteClick.removeObserver(onFavoriteClickObserver)
        inputs.requestNewData.removeObserver(requestNewDataObserver)
        inputs.updateQuote.removeObserver(updateQuoteObserver)
        quoteAddToFavoriteUseCase.dataRequest.removeObserver(onDataRequestObserver)
        quoteShowUseCase.dataRequest.removeObserver(onDataRequestObserver)
    }
}