package com.crisvillamil.platzirecipes.home

sealed class HomeEvent {
    data object OnFetchRecipes : HomeEvent()
    data class OnSearch(val text: String) : HomeEvent()
    data class OnFavoriteClick(val recipeId: Int) : HomeEvent()
}
