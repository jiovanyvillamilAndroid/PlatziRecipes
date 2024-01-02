package com.crisvillamil.platzirecipes.detail

data class DetailUIState(
    val isLoading: Boolean = false,
    val recipeDetailState: RecipeDetailState = RecipeDetailState(),
)