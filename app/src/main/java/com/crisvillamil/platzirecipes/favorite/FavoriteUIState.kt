package com.crisvillamil.platzirecipes.favorite

data class FavoriteUIState(
    val isLoading: Boolean = false,
    val favorites: List<FavoriteItemUIState> = arrayListOf(),
)