package com.crisvillamil.platzirecipes.favorite

data class FavoriteItemUIState(
    val recipeId: Int,
    val imageURL: String,
    val name: String,
    val rate: Float,
    val cookingTime: String,
    val views: String,
)