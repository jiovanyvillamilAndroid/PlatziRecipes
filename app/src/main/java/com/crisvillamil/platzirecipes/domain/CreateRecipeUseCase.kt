package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Difficulty
import com.crisvillamil.platzirecipes.model.Recipe
import com.crisvillamil.platzirecipes.model.Repository

class CreateRecipeUseCase(private val repository: Repository) {
    suspend operator fun invoke(
        name: String,
        description: String,
        imageUrl: String?,
        authorImageUrl: String?,
        rating: Float?,
        authorName: String?,
        ingredients: String,
        cookingSteps: List<String>,
        difficulty: Difficulty?,
        cookingTime: String,
        viewsCount: String,
    ) {
        repository.createRecipe(
            name,
            description,
            imageUrl,
            authorImageUrl,
            rating,
            authorName,
            ingredients,
            cookingSteps,
            difficulty,
            cookingTime,
            viewsCount
        )
    }
}