package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class AddToFavoriteRecipeUseCase(private val repository: Repository) {
    suspend operator fun invoke(recipeId: Int) {
        repository.addRecipeToFavorite(recipeId)
    }
}