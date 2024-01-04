package com.crisvillamil.platzirecipes.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("recipe_id") val recipeId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("author_image_url") val authorImageUrl: String?,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("author_name") val authorName: String?,
    @SerializedName("ingredients") val ingredients: String,
    @SerializedName("cooking_steps") val cookingSteps: List<String>,
    @SerializedName("difficulty")  val difficulty: Difficulty?,
    @SerializedName("cooking_time")  val cookingTime: String,
    @SerializedName("views_count") val viewsCount: String,
)

enum class Difficulty(val text: String) {
    EASY("Fácil"), MEDIUM("Medio"), HARD("Difícil")
}
