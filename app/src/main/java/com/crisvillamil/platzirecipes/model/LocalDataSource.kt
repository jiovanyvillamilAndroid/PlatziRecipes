package com.crisvillamil.platzirecipes.model

interface LocalDataSource {
    suspend fun getRecipes():List<Recipe>
    suspend fun saveRecipes(recipes:List<Recipe>)
    suspend fun saveRecipe(recipe: Recipe)
    suspend fun addToFavorite(recipeId: Int)
    suspend fun removeFromFavorite(recipeId: Int)

    suspend fun isFavorite(recipeId: Int) : Boolean

    suspend fun addRecipeRate(recipeId: Int, rate: Int)

    suspend fun getRateFromRecipe(recipeId: Int): Int?
}