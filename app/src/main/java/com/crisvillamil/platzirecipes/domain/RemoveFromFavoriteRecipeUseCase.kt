package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class RemoveFromFavoriteRecipeUseCase {

    suspend operator fun invoke(recipeId: Int) = Repository.removeRecipeFromFavorites(recipeId)
}