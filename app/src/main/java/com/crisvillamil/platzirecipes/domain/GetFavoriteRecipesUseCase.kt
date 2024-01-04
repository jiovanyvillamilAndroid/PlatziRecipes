package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Recipe
import com.crisvillamil.platzirecipes.model.Repository

class GetFavoriteRecipesUseCase {
    suspend operator fun invoke(): List<Recipe> = Repository.getAllFavoriteRecipes()
}