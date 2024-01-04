package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class AddToFavoriteRecipeUseCase {
    suspend operator fun invoke(recipeId: Int) {
        Repository.addRecipeToFavorite(recipeId)
    }
}