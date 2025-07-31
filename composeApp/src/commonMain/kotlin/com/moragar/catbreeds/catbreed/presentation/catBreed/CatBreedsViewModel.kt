package com.moragar.catbreeds.catbreed.presentation.catBreed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.usecase.GetCatBreedsUseCase
import com.moragar.catbreeds.catbreed.domain.usecase.ReadFavoriteBreedsUseCase
import com.moragar.catbreeds.catbreed.domain.usecase.ToggleFavoriteUseCase
import com.moragar.catbreeds.core.domain.usecase.IsOnlineUseCase
import com.moragar.catbreeds.core.domain.usecase.ReadAllCachedBreedsUseCase
import com.moragar.catbreeds.core.domain.usecase.SaveCachedBreedUseCase
import com.moragar.catbreeds.core.util.Constants.ERROR_LOADING_BREEDS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatBreedsViewModel(
    private val getCatBreedsUseCase: GetCatBreedsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val readFavoriteBreedsUseCase: ReadFavoriteBreedsUseCase,
    private val isOnlineUseCase: IsOnlineUseCase,
    private val readAllCachedBreedsUseCase: ReadAllCachedBreedsUseCase,
    private val saveCachedBreedUseCase: SaveCachedBreedUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _state = MutableStateFlow(CatBreedsState())
    val state = _state.asStateFlow()

    private val _connectivityState = MutableStateFlow(ConnectivityState())
    val connectivityState = _connectivityState.asStateFlow()
    private val _cacheState = MutableStateFlow(CatBreedsState())
    val cacheState = _cacheState.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20

    init {
        viewModelScope.launch(context = dispatcher) {
            isOnlineUseCase.invoke().collect { isOnline ->
                _connectivityState.value = _connectivityState.value.copy(
                    isOnline = isOnline,
                    showOfflineWarning = !isOnline
                )
            }
            readAllCachedBreedsUseCase.invoke().collect { breeds ->
                _cacheState.value = _cacheState.value.copy(
                    breeds = breeds,
                    isCachedData = breeds.isNotEmpty()
                )
            }
        }
    }
    fun onAction(action: CatBreedAction) {
        when (action) {
            is CatBreedAction.LoadInitialBreeds -> loadInitialBreeds()
            is CatBreedAction.LoadNextPage -> loadNextPage()
            is CatBreedAction.ToggleFavorite -> toggleFavorite(action.breedId)
            is CatBreedAction.OnTabSelected -> onTabSelected(action.index)
        }
    }

    private fun loadInitialBreeds() {

        _state.value = _state.value.copy(
            isLoading = true,
            isRefreshing = false,
            endReached = false
        )

        viewModelScope.launch(context = dispatcher) {
            getCatBreedsUseCase(pageSize, currentPage)
                .onStart {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { breeds ->
                            var breedsWithFavorites = breeds
                            readFavoriteBreedsUseCase.invoke().collect { favorites ->
                                breedsWithFavorites = breeds.map {
                                    it.copy(isFavorite = it.id in favorites)
                                }
                            }
                            val cacheIds = cacheState.value.breeds.map { it.id }
                            val newCachedBreeds = breedsWithFavorites.filter { !cacheIds.contains(it.id) }
                            newCachedBreeds.forEach { breed ->
                                saveCachedBreedUseCase.invoke(breed)
                            }
                            _state.update { it ->
                                it.copy(
                                    breeds = breedsWithFavorites,
                                    favorites = breedsWithFavorites.filter { it.isFavorite },
                                    isLoading = false,
                                    currentPage = 1,
                                    endReached = breeds.size < pageSize
                                )
                            }
                            currentPage = 0
                        },
                        onFailure = { error ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = error.message ?: ERROR_LOADING_BREEDS
                                )
                            }
                        }
                    )
                }
        }
    }

    private fun loadNextPage() {

        _state.value = _state.value.copy(isRefreshing = true)
        val nextPage = currentPage + 1

        viewModelScope.launch(context = dispatcher) {
            getCatBreedsUseCase(pageSize, nextPage)
                .collect { result ->
                    result.fold(
                        onSuccess = { breeds ->
                            if (breeds.isEmpty()) {
                                _state.update { it.copy(endReached = true) }
                            } else {
                                val updatedBreeds = _state.value.breeds + breeds
                                _state.update {
                                    it.copy(
                                        breeds = updatedBreeds,
                                        isRefreshing = false,
                                        currentPage = nextPage,
                                        endReached = breeds.size < pageSize
                                    )
                                }
                                currentPage = nextPage
                            }
                        },
                        onFailure = { error ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Page $nextPage: ${error.message ?: "Error"}"
                                )
                            }
                        }
                    )
                }
        }
    }

    private fun toggleFavorite(breedId: String) {
        viewModelScope.launch(context = dispatcher) {
            toggleFavoriteUseCase(breedId)
            val breed = _state.value.breeds.find { it.id == breedId } ?: CatBreed()
            _state.value = _state.value.copy(
                breeds = _state.value.breeds.map { breed ->
                    if (breed.id == breedId) breed.copy(isFavorite = !breed.isFavorite)
                    else breed
                },
                favorites = if (!breed.isFavorite) _state.value.favorites + breed
                else _state.value.favorites - breed
            )
        }
    }

    private fun onTabSelected(index: Int) {
        _state.value = _state.value.copy(selectedTabIndex = index)
    }
}

data class CatBreedsState(
    val breeds: List<CatBreed> = emptyList(),
    val favorites: List<CatBreed> = emptyList(),
    val currentCatBreed: CatBreed = CatBreed(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0,
    val selectedTabIndex: Int = 0,
    val isCachedData: Boolean = false
)

data class ConnectivityState(
    val isOnline: Boolean = true,
    val showOfflineWarning: Boolean = false
)