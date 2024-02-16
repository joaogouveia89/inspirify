package io.github.joaogouveia89.inspirify.ui.quoteShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.data.api.asQuote
import io.github.joaogouveia89.inspirify.data.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.data.local.LocalDb
import javax.inject.Inject

class QuoteShowRepository @Inject constructor(
    private val api: RetrofitZenQuotes,
    private val localDb: LocalDb
)  {

    private val _dataRequest = MutableLiveData<DataRequest>()

    val dataRequest: LiveData<DataRequest>
        get() = _dataRequest

    suspend fun fetchRandomQuote() {
        _dataRequest.postValue(DataRequest.OnProgress)
        try {
            val response = api.fetchRandomQuote()
            if (response.isSuccessful) {
                response.body()?.let {
                    _dataRequest.postValue(DataRequest.Success(it.first().asQuote()))
                }
            }
        }catch (e: Exception) {
            e.message?.let { errorMessage ->
                _dataRequest.postValue(DataRequest.Failed(errorMessage))
            }
        }
    }
}