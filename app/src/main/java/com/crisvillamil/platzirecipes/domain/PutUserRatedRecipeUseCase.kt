package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Repository

class PutUserRatedRecipeUseCase(private val repository: Repository) {

    suspend operator fun invoke(recipeId: Int, rate: Int) = repository.putRecipeRate(recipeId, rate)
}