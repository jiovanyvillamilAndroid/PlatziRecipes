package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Recipe
import com.crisvillamil.platzirecipes.model.Repository

class GetRecipesUseCase {
    suspend operator fun invoke(): List<Recipe> = Repository.getAllRecipes()
}