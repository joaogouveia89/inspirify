package io.github.joaogouveia89.inspirify.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.joaogouveia89.inspirify.data.DataRequest
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
    val favoritesRequestStatus: LiveData<DataRequest>
    val removeFromFavoritesStatus: LiveData<DataRequest>
    val showFavoritesList: LiveData<Boolean>
    val showEmptyListMessage: LiveData<Boolean>
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

    @Inject
    lateinit var fetchFavoritesUseCase: FetchFavoritesUseCase

    init {
        inspirifyComponent.inject(this)
        inputs.requestNewData.observeForever(requestNewDataObserver)
        inputs.onFavoriteDelete.observeForever(onFavoriteDeleteObserver)
    }

    override val favoritesRequestStatus: LiveData<DataRequest> = fetchFavoritesUseCase.dataRequest
    override val removeFromFavoritesStatus: LiveData<DataRequest> = MutableLiveData()
    override val showFavoritesList: LiveData<Boolean> = MutableLiveData()
    override val showEmptyListMessage: LiveData<Boolean> = MutableLiveData()

    private suspend fun fetchAllFavorites() {
        fetchFavoritesUseCase.execute()
    }

}