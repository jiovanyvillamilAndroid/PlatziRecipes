package com.crisvillamil.platzirecipes.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDAO {

    @Query("SELECT * FROM recipes")
    suspend fun getAll(): List<DataBaseRecipe>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dataBaseRecipe: DataBaseRecipe)

    @Query("UPDATE recipes SET is_favorite=1 WHERE recipe_id = :recipeId")
    suspend fun addToFavorite(recipeId: Int)

    @Query("UPDATE recipes SET is_favorite=0 WHERE recipe_id = :recipeId")
    suspend fun removeFromFavorites(recipeId: Int)

    @Query("SELECT EXISTS(SELECT * FROM recipes WHERE recipe_id = :recipeId AND is_favorite = 1)")
    suspend fun isFavorite(recipeId: Int) : Boolean

    @Query("UPDATE recipes SET user_rate=:rate WHERE recipe_id = :recipeId")
    suspend fun addUserRate(recipeId: Int, rate: Int)

    @Query("SELECT user_rate FROM recipes WHERE recipe_id = :recipeId ")
    fun getRecipeUserRate(recipeId: Int): Int?

}