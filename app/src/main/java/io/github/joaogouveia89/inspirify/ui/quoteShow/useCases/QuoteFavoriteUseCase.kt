package io.github.joaogouveia89.inspirify.ui.quoteShow.useCases

import androidx.lifecycle.LiveData
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowRepository
import javax.inject.Inject

class QuoteFavoriteUseCase @Inject constructor(
    private val quoteShowRepository: QuoteShowRepository
){
    val dataRequest: LiveData<DataRequest>
        get() = quoteShowRepository.dataRequest

    suspend fun execute() = quoteShowRepository.fetchRandomQuote()
}