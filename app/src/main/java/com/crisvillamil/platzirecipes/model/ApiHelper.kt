package com.crisvillamil.platzirecipes.model

class ApiHelper(private val recipeApiClient: RecipeApiClient) {

    suspend fun getAllRecipes() = recipeApiClient.getAllRecipes()
}