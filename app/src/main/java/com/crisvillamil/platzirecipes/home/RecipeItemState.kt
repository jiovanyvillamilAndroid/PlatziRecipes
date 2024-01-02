package com.crisvillamil.platzirecipes.home

data class RecipeItemState(
    val recipeId: Int,
    val title: String,
    val imageUrl: String,
    val authorName: String,
    val authorImageUrl: String,
    val rating: String,
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
)