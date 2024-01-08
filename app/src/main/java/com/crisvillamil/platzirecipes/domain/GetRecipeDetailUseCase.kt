package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class GetRecipeDetailUseCase(private val repository: Repository) {
    suspend operator fun invoke(recipeId: Int) =
        repository.getAllRecipes().first { it.recipeId == recipeId }
}