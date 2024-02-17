package io.github.joaogouveia89.inspirify.ui.quoteShow.useCases

import androidx.lifecycle.LiveData
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.ui.quoteShow.Quote
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteRepository
import javax.inject.Inject

class QuoteFavoriteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    val dataRequest: LiveData<DataRequest>
        get() = quoteRepository.dataRequest

    suspend fun execute(quote: Quote) = quoteRepository.addFavorite(quote)
}