package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class GetRecipeDetailUseCase {
    suspend operator fun invoke(recipeId: Int) =
        Repository.getAllRecipes().first { it.recipeId == recipeId }
}