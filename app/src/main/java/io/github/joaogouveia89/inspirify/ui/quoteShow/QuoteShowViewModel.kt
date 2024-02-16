package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.joaogouveia89.inspirify.R

class QuotesInputs{
    val onRefresh = MutableLiveData<Unit>()
    val onFavoriteClick = MutableLiveData<Unit>()
}

interface QuotesOutputs{
    val quote: LiveData<Quote>
}

interface QuoteViewModelType{
    val inputs: QuotesInputs
    val outputs: QuotesOutputs
}
class QuoteShowViewModel: ViewModel(), QuoteViewModelType, QuotesOutputs {

    override val outputs = this
    override val inputs = QuotesInputs()

    val currentQuote = Quote(
        message = "If you find yourself criticizing other people, you’re probably doing it out of resistance. When we see others beginning to live their authentic selves, it drives us crazy if we have not lived out our own.",
        author = "The Art of War",
        favoriteIconRes = R.drawable.ic_like
    )

    override val quote: LiveData<Quote> = MutableLiveData(currentQuote)
}