package com.crisvillamil.platzirecipes.detail

sealed class DetailEvent {
    data object OnFavoriteClicked:DetailEvent()
    data class OnRateSelected(val rate: Int) : DetailEvent()
    data object OnFetchRecipeDetail : DetailEvent()
}