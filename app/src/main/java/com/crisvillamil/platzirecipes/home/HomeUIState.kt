package com.crisvillamil.platzirecipes.home

data class HomeUIState(
    val isLoading : Boolean = false,
    val recipes : List<RecipeItemState> = arrayListOf(),
)
