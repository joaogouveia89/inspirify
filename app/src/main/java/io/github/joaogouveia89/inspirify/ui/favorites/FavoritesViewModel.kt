package io.github.joaogouveia89.inspirify.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.github.joaogouveia89.inspirify.ui.favorites.useCases.DeleteFavoritesUseCase
import io.github.joaogouveia89.inspirify.ui.favorites.useCases.FetchFavoritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesInputs {
    val requestNewData = MutableLiveData<Unit>()
    val onFavoriteDelete = MutableLiveData<Long>()
}

interface FavoritesOutputs {
    val currentFavoritesList: LiveData<List<Favorite>>
    val showEmptyListMessage: LiveData<Boolean>
    val fetchingInProgress: LiveData<Boolean>
    val showErrorMessage: LiveData<String>
}

interface FavoritesViewModelType {
    val inputs: FavoritesInputs
    val outputs: FavoritesOutputs
}

class FavoritesViewModel  @Inject constructor(
    private val fetchFavoritesUseCase: FetchFavoritesUseCase,
    private val deleteFavoritesUseCase: DeleteFavoritesUseCase,
) : ViewModel(),
    FavoritesViewModelType, FavoritesOutputs {

    override val outputs = this
    override val inputs = FavoritesInputs()


    private val requestNewDataObserver = Observer<Unit> {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAllFavorites()
        }
    }

    private val onFavoriteDeleteObserver = Observer<Long> { favoriteId ->
        viewModelScope.launch(Dispatchers.IO) {
            deleteFavoriteById(favoriteId)
        }
    }

    private val favoritesUpdateObserver = Observer<DataRequest> { response ->
        when (response) {
            is DataRequest.OnProgress -> {
                _fetchingInProgress.value = true
            }

            is DataRequest.Success<*> -> {
                val favorites = response.data as? List<Favorite>
                val deleteCode = response.data as? Int
                _fetchingInProgress.value = false
                // binding.quote = quote
                favorites?.let {
                    _showEmptyListMessage.value = it.isEmpty()
                    _currentFavoritesList.value = it
                }

                deleteCode?.let {
                    if (it == 1) {
                        _currentFavoritesList.value?.filter { favorite ->
                            favorite.id != inputs.onFavoriteDelete.value
                        }?.let { newFavoritesList ->
                            _currentFavoritesList.postValue(newFavoritesList)
                        }
                    }
                }
            }

            is DataRequest.Failed -> {
                _fetchingInProgress.postValue(false)
                _showErrorMessage.postValue(response.errorMessage)
            }
        }

    }

    init {
        inputs.requestNewData.observeForever(requestNewDataObserver)
        inputs.onFavoriteDelete.observeForever(onFavoriteDeleteObserver)
        fetchFavoritesUseCase.dataRequest.observeForever(favoritesUpdateObserver)
        deleteFavoritesUseCase.dataRequest.observeForever(favoritesUpdateObserver)
    }

    private val _currentFavoritesList = MutableLiveData<List<Favorite>>()
    private val _showErrorMessage = MutableLiveData<String>()
    private val _showEmptyListMessage = MutableLiveData(true)
    private val _fetchingInProgress = MutableLiveData(false)

    override val currentFavoritesList: LiveData<List<Favorite>>
        get() = _currentFavoritesList

    override val showErrorMessage: LiveData<String>
        get() = _showErrorMessage

    override val showEmptyListMessage: LiveData<Boolean>
        get() = _showEmptyListMessage

    override val fetchingInProgress: LiveData<Boolean>
        get() = _fetchingInProgress

    private suspend fun fetchAllFavorites() {
        fetchFavoritesUseCase.execute()
    }

    private suspend fun deleteFavoriteById(id: Long) {
        deleteFavoritesUseCase.execute(id)
    }

    override fun onCleared() {
        super.onCleared()
        fetchFavoritesUseCase.dataRequest.removeObserver(favoritesUpdateObserver)
        deleteFavoritesUseCase.dataRequest.removeObserver(favoritesUpdateObserver)
        inputs.onFavoriteDelete.removeObserver(onFavoriteDeleteObserver)
        inputs.requestNewData.removeObserver(requestNewDataObserver)
    }

}