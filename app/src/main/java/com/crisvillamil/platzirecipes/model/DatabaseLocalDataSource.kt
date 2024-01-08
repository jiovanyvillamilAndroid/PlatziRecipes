package com.crisvillamil.platzirecipes.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.crisvillamil.platzirecipes.model.database.AppDatabase
import com.crisvillamil.platzirecipes.model.database.DataBaseRecipe

class DatabaseLocalDataSource(appDatabase: AppDatabase) :
    LocalDataSource {

    private val recipeDao = appDatabase.recipeDao()
    override suspend fun getRecipes(): List<Recipe> = recipeDao.getAll().map {
        Recipe(
            it.recipeId,
            it.name,
            it.description,
            it.imageUrl,
            it.authorImageUrl,
            it.rating,
            it.authorName,
            it.ingredients,
            it.cookingSteps,
            it.difficulty,
            it.cookingTime,
            it.viewsCount
        )
    }


    override suspend fun saveRecipes(recipes: List<Recipe>) {
        recipes.forEach {
            saveRecipe(it)
        }
    }

    override suspend fun saveRecipe(recipe: Recipe) {
        val dataBaseRecipe = DataBaseRecipe(
            recipe.recipeId,
            recipe.name,
            recipe.description,
            recipe.imageUrl,
            recipe.authorImageUrl,
            recipe.rating,
            recipe.authorName,
            recipe.ingredients,
            recipe.cookingSteps,
            recipe.difficulty,
            recipe.cookingTime,
            recipe.viewsCount,
            false,
            null
        )
        recipeDao.insert(dataBaseRecipe)
    }

    override suspend fun addToFavorite(recipeId: Int) {
        recipeDao.addToFavorite(recipeId)
    }

    override suspend fun removeFromFavorite(recipeId: Int) {
        recipeDao.removeFromFavorites(recipeId)
    }

    override suspend fun isFavorite(recipeId: Int): Boolean = recipeDao.isFavorite(recipeId)


    override suspend fun addRecipeRate(recipeId: Int, rate: Int) {
        recipeDao.addUserRate(recipeId,rate)
    }

    override suspend fun getRateFromRecipe(recipeId: Int): Int? = recipeDao.getRecipeUserRate(recipeId)
}