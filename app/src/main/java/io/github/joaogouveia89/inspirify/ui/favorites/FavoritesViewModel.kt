package io.github.joaogouveia89.inspirify.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.github.joaogouveia89.inspirify.ui.favorites.useCases.FetchFavoritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesInputs {
    val requestNewData = MutableLiveData<Unit>()
    val onFavoriteDelete = MutableLiveData<Int>()
}

interface FavoritesOutputs {
    val currentFavoritesList: LiveData<List<Favorite>>
    val showEmptyListMessage: LiveData<Boolean>
    val showErrorMessage: LiveData<String>
}

interface FavoritesViewModelType {
    val inputs: FavoritesInputs
    val outputs: FavoritesOutputs
}

class FavoritesViewModel(inspirifyComponent: InspirifyComponent) : ViewModel(),
    FavoritesViewModelType, FavoritesOutputs {

    override val outputs = this
    override val inputs = FavoritesInputs()


    private val requestNewDataObserver = Observer<Unit> {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAllFavorites()
        }
    }

    private val onFavoriteDeleteObserver = Observer<Int> {

    }

    private val fetchFavoritesObserver = Observer<DataRequest> { response ->
        when (response) {
            is DataRequest.OnProgress -> {
                //binding.progressBar.visibility = View.VISIBLE
            }

            is DataRequest.Success<*> -> {
                val favorites = response.data as? List<Favorite>
                // binding.quote = quote
                favorites?.let {
                    _showEmptyListMessage.value = it.isEmpty()
                    _currentFavoritesList.value = it
                }
            }

            is DataRequest.Failed -> {
                _showErrorMessage.postValue(response.errorMessage)
            }
        }

    }

    @Inject
    lateinit var fetchFavoritesUseCase: FetchFavoritesUseCase

    init {
        inspirifyComponent.inject(this)
        inputs.requestNewData.observeForever(requestNewDataObserver)
        inputs.onFavoriteDelete.observeForever(onFavoriteDeleteObserver)
        fetchFavoritesUseCase.dataRequest.observeForever(fetchFavoritesObserver)
    }

    private val _currentFavoritesList = MutableLiveData<List<Favorite>>()
    private val _showErrorMessage = MutableLiveData<String>()
    private val _showEmptyListMessage = MutableLiveData(true)
    override val currentFavoritesList: LiveData<List<Favorite>>
        get() = _currentFavoritesList

    override val showErrorMessage: LiveData<String>
        get() = _showErrorMessage

    override val showEmptyListMessage: LiveData<Boolean>
        get() = _showEmptyListMessage

    private suspend fun fetchAllFavorites() {
        fetchFavoritesUseCase.execute()
    }

    override fun onCleared() {
        super.onCleared()
        fetchFavoritesUseCase.dataRequest.removeObserver(fetchFavoritesObserver)
    }

}