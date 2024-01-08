package com.crisvillamil.platzirecipes.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DataBaseRecipe::class], version = 1)
@TypeConverters(CookingStepsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDAO
}