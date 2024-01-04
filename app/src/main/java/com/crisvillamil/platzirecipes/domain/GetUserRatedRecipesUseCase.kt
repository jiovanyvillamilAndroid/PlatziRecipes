package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class GetUserRatedRecipesUseCase {

    suspend operator fun invoke(recipeId: Int): Int? = Repository.getRecipeRate(recipeId)
}