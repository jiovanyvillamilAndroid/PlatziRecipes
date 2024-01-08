package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class GetIsFavoriteRecipeUseCase(private val repository: Repository) {
    suspend operator fun invoke(recipeId: Int?): Boolean =
        if (recipeId != null) repository.isRecipeFavorite(recipeId) else false
}