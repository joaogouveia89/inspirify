package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.joaogouveia89.inspirify.R
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.data.api.asQuote
import io.github.joaogouveia89.inspirify.data.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.data.local.LocalDb
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite
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
                            localDb.favoriteDao().countReincidenceOfQuotes(
                                quote = quote.quote,
                                author = quote.author
                            )
                        }
                        _dataRequest.postValue(DataRequest.Success(quote.asQuote(
                            isFavorite = numberOfIncidences > 0
                        )))
                    }
                }
            }
        } catch (e: Exception) {
            e.message?.let { errorMessage ->
                _dataRequest.postValue(DataRequest.Failed(errorMessage))
            }
        }
    }

    suspend fun addFavorite(quote: Quote){
        _dataRequest.postValue(DataRequest.OnProgress)

        val code = localDb.favoriteDao().addToFavorites(quote.asFavorite())

        if(code == -1L) _dataRequest.postValue(DataRequest.Failed("Insert failed"))
        else {
            val quoteWithFavoriteSign = quote.copy(
                    favoriteIconRes = R.drawable.ic_like_fill
            )
            _dataRequest.postValue(DataRequest.Success(quoteWithFavoriteSign))
        }
    }
}