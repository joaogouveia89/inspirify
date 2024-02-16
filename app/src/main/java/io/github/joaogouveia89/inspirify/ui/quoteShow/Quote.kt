package io.github.joaogouveia89.inspirify.ui.quoteShow

data class Quote(
    val quote: String,
    val author: String,
    val isFavorite: Boolean = false
)
