package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class GetUserRatedRecipesUseCase(private val repository: Repository) {

    suspend operator fun invoke(recipeId: Int): Int? = repository.getRecipeRate(recipeId)
}