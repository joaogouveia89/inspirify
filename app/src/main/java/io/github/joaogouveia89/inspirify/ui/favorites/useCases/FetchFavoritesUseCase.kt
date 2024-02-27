package io.github.joaogouveia89.inspirify.ui.favorites.useCases

import androidx.lifecycle.LiveData
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.data.repositories.QuoteRepository
import javax.inject.Inject

class FetchFavoritesUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {

    val dataRequest: LiveData<DataRequest>
        get() = quoteRepository.dataRequest

    suspend fun execute() = quoteRepository.getAllFavorites()

}