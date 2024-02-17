package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.annotation.DrawableRes
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite

data class Quote(
    val message: String,
    val author: String,
    @DrawableRes val favoriteIconRes: Int
)

fun Quote.asFavorite(id: Long = 0) = Favorite(
    id = id,
    quote = this.message,
    author = this.author
)
