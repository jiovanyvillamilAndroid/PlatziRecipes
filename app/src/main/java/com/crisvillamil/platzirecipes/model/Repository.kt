package com.crisvillamil.platzirecipes.model

import kotlinx.coroutines.delay

class Repository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RecipesService
) {
    suspend fun getAllRecipes(): List<Recipe> {
        delay(300L)//TODO: remove this
        //val response = remoteDataSource.getRecipes()
        val fakeDataProvider = FakeDataProvider()//TODO: Remove this
        val response = fakeDataProvider.getRecipes()//TODO: Remove this
        if (response.isNotEmpty()) {
            localDataSource.saveRecipes(response)
        }
        return localDataSource.getRecipes()
    }

    suspend fun createRecipe(
        name: String,
        description: String,
        imageUrl: String?,
        authorImageUrl: String?,
        rating: Float?,
        authorName: String?,
        ingredients: String,
        cookingSteps: List<String>,
        difficulty: Difficulty?,
        cookingTime: String,
        viewsCount: String,
    ) {
        delay(300L)
        remoteDataSource.postRecipe(
            name,
            description,
            imageUrl,
            authorImageUrl,
            rating,
            authorName,
            ingredients,
            cookingSteps,
            difficulty,
            cookingTime,
            viewsCount
        )
    }

    suspend fun removeRecipeFromFavorites(recipeId: Int) {
        localDataSource.removeFromFavorite(recipeId)
    }

    suspend fun addRecipeToFavorite(recipeId: Int) {
        localDataSource.addToFavorite(recipeId)
    }

    suspend fun getRecipeRate(recipeId: Int): Int? {
        return localDataSource.getRateFromRecipe(recipeId)
    }

    suspend fun putRecipeRate(recipeId: Int, rate: Int) {
        localDataSource.addRecipeRate(recipeId, rate)
    }

    suspend fun isRecipeFavorite(recipeId: Int) = localDataSource.isFavorite(recipeId)

    suspend fun getAllFavoriteRecipes(): List<Recipe> =
        localDataSource.getRecipes().filter {
            isRecipeFavorite(
                it.recipeId
            )
        }
}
