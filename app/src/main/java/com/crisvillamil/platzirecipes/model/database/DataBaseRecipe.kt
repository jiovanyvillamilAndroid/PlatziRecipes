package com.crisvillamil.platzirecipes.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.crisvillamil.platzirecipes.model.Difficulty
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "recipes")
data class DataBaseRecipe(
    @PrimaryKey @ColumnInfo("recipe_id") val recipeId: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("image_url") val imageUrl: String?,
    @ColumnInfo("author_image_url") val authorImageUrl: String?,
    @ColumnInfo("rating") val rating: Float?,
    @ColumnInfo("author_name") val authorName: String?,
    @ColumnInfo("ingredients") val ingredients: String,
    @ColumnInfo("cooking_steps") val cookingSteps: List<String>,
    @ColumnInfo("difficulty") val difficulty: Difficulty?,
    @ColumnInfo("cooking_time") val cookingTime: String,
    @ColumnInfo("views_count") val viewsCount: String,
    @ColumnInfo("is_favorite") val isFavorite: Boolean = false,
    @ColumnInfo("user_rate") val userRate: Int?
)

class CookingStepsConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): List<String?> {
        val listType = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String?>): String = gson.toJson(list)


}