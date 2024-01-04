package com.crisvillamil.platzirecipes.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface RecipeApiClient {
    @GET("recipes")
    suspend fun getAllRecipes(): Response<List<Recipe>>

    @POST("recipes")
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
        viewsCount: String
    ): Response<Boolean>
}