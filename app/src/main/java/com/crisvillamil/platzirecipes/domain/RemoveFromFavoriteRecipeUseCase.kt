package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class RemoveFromFavoriteRecipeUseCase(private val repository: Repository) {

    suspend operator fun invoke(recipeId: Int) = repository.removeRecipeFromFavorites(recipeId)
}