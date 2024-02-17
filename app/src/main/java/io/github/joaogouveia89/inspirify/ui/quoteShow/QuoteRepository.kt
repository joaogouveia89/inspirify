package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.joaogouveia89.inspirify.R
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.data.api.asQuote
import io.github.joaogouveia89.inspirify.data.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.data.local.LocalDb
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val api: RetrofitZenQuotes,
    private val localDb: LocalDb
) {

    private val _dataRequest = MutableLiveData<DataRequest>()

    val dataRequest: LiveData<DataRequest>
        get() = _dataRequest

    suspend fun fetchRandomQuote() {
        _dataRequest.postValue(DataRequest.OnProgress)
        try {
            val response = api.fetchRandomQuote()
            if (response.isSuccessful) {
                //checking if the quote is already on favorites
                response.body()?.let {
                    it.first().let { quote ->
                        val numberOfIncidences = runBlocking {
                            localDb.favoriteDao().getQuoteLocally(
                                quote = quote.quote,
                                author = quote.author
                            )
                        }
                        _dataRequest.postValue(
                            DataRequest.Success(
                                quote.asQuote(
                                    isFavorite = numberOfIncidences.isNotEmpty()
                                )
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.message?.let { errorMessage ->
                _dataRequest.postValue(DataRequest.Failed(errorMessage))
            }
        }
    }

    suspend fun addFavorite(quote: Quote) {
        _dataRequest.postValue(DataRequest.OnProgress)

        val localQuote = runBlocking {
            localDb.favoriteDao().getQuoteLocally(
                quote = quote.message,
                author = quote.author
            )
        }

        if (localQuote.isNotEmpty()) {
            val code =
                localDb.favoriteDao().deleteFromFavorites(quote.asFavorite(localQuote.first().id))
            if (code == -1) _dataRequest.postValue(DataRequest.Failed("Delete failed"))
            else {
                val quoteWithFavoriteSign = quote.copy(
                    favoriteIconRes = R.drawable.ic_like
                )
                _dataRequest.postValue(DataRequest.Success(quoteWithFavoriteSign))
            }
        } else {
            val code = localDb.favoriteDao().addToFavorites(quote.asFavorite())

            if (code == -1L) _dataRequest.postValue(DataRequest.Failed("Insert failed"))
            else {
                val quoteWithFavoriteSign = quote.copy(
                    favoriteIconRes = R.drawable.ic_like_fill
                )
                _dataRequest.postValue(DataRequest.Success(quoteWithFavoriteSign))
            }
        }
    }
}