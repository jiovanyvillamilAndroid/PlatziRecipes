package com.crisvillamil.platzirecipes.model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesService(private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private val retrofit = RetrofitHelper.getRetrofit().create(RecipeApiClient::class.java)
    suspend fun getRecipes(): List<Recipe> {
        return withContext(defaultDispatcher) {
            val response = retrofit.getAllRecipes()
            response.body().orEmpty()
        }
    }

    suspend fun postRecipe(
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
        withContext(defaultDispatcher) {
            retrofit.createRecipe(
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
    }

}