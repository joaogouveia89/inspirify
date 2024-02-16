package io.github.joaogouveia89.inspirify.ui.quoteShow.useCases

import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowRepository
import javax.inject.Inject

class QuoteShowUseCase @Inject constructor(
    private val quoteShowRepository: QuoteShowRepository
) {
}