package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.annotation.DrawableRes

data class Quote(
    val message: String,
    val author: String,
    @DrawableRes val favoriteIconRes: Int
)
