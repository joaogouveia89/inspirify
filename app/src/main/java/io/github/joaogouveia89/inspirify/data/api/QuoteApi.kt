package io.github.joaogouveia89.inspirify.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.github.joaogouveia89.inspirify.R
import io.github.joaogouveia89.inspirify.ui.quoteShow.Quote
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuoteApi(
    @SerializedName("q") val quote: String,
    @SerializedName("a") val author: String
): Parcelable

fun QuoteApi.asQuote() = Quote(
    message = this.quote,
    author = this.author,
    favoriteIconRes = R.drawable.ic_like
)