package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class GetIsFavoriteRecipeUseCase {
    suspend operator fun invoke(recipeId: Int?): Boolean =
        if (recipeId != null) Repository.isRecipeFavorite(recipeId) else false
}