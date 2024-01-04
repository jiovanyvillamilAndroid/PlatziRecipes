package com.crisvillamil.platzirecipes.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDAO {

    @Query("SELECT * FROM recipe")
    fun getAll():List<Recipe>

    @Insert
    fun insertAll(vararg recipe: Recipe)

}