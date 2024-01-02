package com.crisvillamil.platzirecipes.favorite

sealed class FavoriteEvent {
    data class OnRemoveFromFavorites(val recipeId:Int) : FavoriteEvent()
    data object OnFetchFavoriteRecipes : FavoriteEvent()
}