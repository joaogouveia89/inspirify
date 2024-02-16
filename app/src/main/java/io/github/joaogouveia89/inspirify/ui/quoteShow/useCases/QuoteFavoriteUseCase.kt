package io.github.joaogouveia89.inspirify.ui.quoteShow.useCases

import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowRepository
import javax.inject.Inject

class QuoteFavoriteUseCase @Inject constructor(
    private val quoteShowRepository: QuoteShowRepository
)