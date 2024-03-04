package io.github.joaogouveia89.inspirify.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.joaogouveia89.inspirify.R
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.data.api.asQuote
import io.github.joaogouveia89.inspirify.data.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.data.local.LocalDb
import io.github.joaogouveia89.inspirify.ui.quoteShow.Quote
import io.github.joaogouveia89.inspirify.ui.quoteShow.asFavorite
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
            } else {
                _dataRequest.postValue(
                    DataRequest.Failed(
                        response.errorBody()?.string() ?: "Unknown error occurred"
                    )
                )
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

    suspend fun getAllFavorites() {
        _dataRequest.postValue(DataRequest.OnProgress)
        runBlocking {
            val favorites = localDb.favoriteDao().getAll()
            _dataRequest.postValue(DataRequest.Success(favorites))
        }
    }

    suspend fun deleteFavorite(id: Long) {
        _dataRequest.postValue(DataRequest.OnProgress)
        runBlocking {
            val code = localDb.favoriteDao().deleteFavoriteById(id)
            if (code > 0) {
                _dataRequest.postValue(DataRequest.Success(code))
            } else {
                _dataRequest.postValue(DataRequest.Failed("Error deleting the favorite"))
            }
        }
    }
}