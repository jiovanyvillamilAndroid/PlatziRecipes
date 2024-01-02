package com.crisvillamil.platzirecipes.detail

import com.crisvillamil.platzirecipes.model.Difficulty

data class RecipeDetailState(
    val imageUrl: String? = null,
    val name: String? = null,
    val rate: Float? = null,
    val isFavorite: Boolean = false,
    val ingredients: String? = null,
    val cookingTime: String? = null,
    val cookingSteps: List<String> = emptyList(),
    val ratingBarValue: Int = 0,
    val difficulty: Difficulty? = null,
)