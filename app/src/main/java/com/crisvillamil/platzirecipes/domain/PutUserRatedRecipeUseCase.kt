package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class PutUserRatedRecipeUseCase {

    suspend operator fun invoke(recipeId: Int, rate: Int) = Repository.putRecipeRate(recipeId, rate)
}