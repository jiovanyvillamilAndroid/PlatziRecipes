package com.crisvillamil.platzirecipes.domain

import com.crisvillamil.platzirecipes.model.Recipe
import com.crisvillamil.platzirecipes.model.Repository

class GetRecipesUseCase(private val repository: Repository) {
    suspend operator fun invoke(): List<Recipe> = repository.getAllRecipes()
}