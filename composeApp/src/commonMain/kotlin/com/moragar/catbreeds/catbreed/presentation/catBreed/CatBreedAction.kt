package com.moragar.catbreeds.catbreed.presentation.catBreed

sealed interface CatBreedAction {
    data object LoadInitialBreeds: CatBreedAction
    data object LoadNextPage: CatBreedAction
    data class ToggleFavorite(val breedId: String): CatBreedAction
    data class OnTabSelected(val index: Int): CatBreedAction

}