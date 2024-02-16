package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    init {
        inspirifyComponent.inject(this)
    }

    override val outputs = this
    override val inputs = QuotesInputs()

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


}