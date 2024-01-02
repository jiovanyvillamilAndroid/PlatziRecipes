package com.crisvillamil.platzirecipes.model

data class Recipe(
    val recipeId: Int,
    val name: String,
    val imageUrl: String?,
    val authorImageUrl: String?,
    val rating: Float?,
    val authorName: String?,
    val ingredients: String,
    val cookingSteps: List<String>,
    val difficulty: Difficulty?,
    val cookingTime: String,
    val viewsCount: String,
)

enum class Difficulty(val text: String) {
    EASY("Fácil"), MEDIUM("Medio"), HARD("Difícil")
}
