package com.crisvillamil.platzirecipes.model

class FakeDataProvider : LocalDataSource {

    private val fakeLocalPreferences by lazy { FakeLocalPreferences() }

    private var recipesLocalData: ArrayList<Recipe> =
        ArrayList<Recipe>().apply {
            repeat(30) {
                add(
                    Recipe(
                        recipeId = it + 1,
                        name = "Receta ${it + 1}",
                        description = "Indonesian Fried Chicken or Ayam Goreng, is a delicious and popular dish that showcases the vibrant flavors of Indonesian cuisine.",
                        imageUrl = arrayListOf(
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTrQ9APTA33CDFgSrc7xSLvB3jy6xk0FbK2lg&usqp=CAU",
                            "https://www.recetasnestle.com.co/sites/default/files/srh_recipes/f78cf6630b31638994b09b3b470b085c.jpg",
                            "https://cloudfront-us-east-1.images.arcpublishing.com/elespectador/LXNJRRHHR5ADHN6LCILCEELBO4.jpg"
                        ).random(),
                        authorImageUrl = "https://freddyvega.com/content/images/size/w2000/2020/08/freddy-vega-grande.jpg",
                        rating = .0f + it * 0.1f,  // alternate null ratings
                        authorName = "Freddy Vega",
                        ingredients = generateIngredientsList(),
                        cookingSteps = generateCookingStepsList(),
                        difficulty = Difficulty.entries.random(),
                        cookingTime = "${(1..10).random()}h ${(0..60).random()}min",
                        viewsCount = "${(0..10).random()}.${(0..10).random()}K"
                    )
                )
            }
        }


    private fun generateIngredientsList(): String {
        var ingredients = ""
        repeat(5) {
            ingredients += "Ingrediente ${it + 1}, ${(it + 1) * 20} gramos - "
        }
        return ingredients
    }


    // Function to generate a random list of cooking steps for each recipe
    private fun generateCookingStepsList(): List<String> {
        return List((3..6).random()) {
            "Haz algun paso de cocina ${it + 1}"
        }
    }


    override suspend fun getRecipes(): List<Recipe> = recipesLocalData

    override suspend fun saveRecipes(recipes: List<Recipe>) {
        recipesLocalData = ArrayList(recipes)
    }

    override suspend fun saveRecipe(recipe: Recipe) {
        recipesLocalData.add(recipe)
    }

    override suspend fun addToFavorite(recipeId: Int) {
        fakeLocalPreferences.userFavoriteRecipe.add(recipeId)
    }

    override suspend fun removeFromFavorite(recipeId: Int) {
        fakeLocalPreferences.userFavoriteRecipe.remove(recipeId)
    }

    override suspend fun isFavorite(recipeId: Int) =
        fakeLocalPreferences.userFavoriteRecipe.contains(recipeId)


    override suspend fun addRecipeRate(recipeId: Int, rate: Int) {
        fakeLocalPreferences.userRatedRecipes[recipeId] = rate
    }

    override suspend fun getRateFromRecipe(recipeId: Int): Int? {
        return if (fakeLocalPreferences.userRatedRecipes.contains(recipeId)) {
            fakeLocalPreferences.userRatedRecipes[recipeId]
        } else {
            null
        }
    }

    data class FakeLocalPreferences(
        val userRatedRecipes: HashMap<Int, Int> = hashMapOf(),
        val userFavoriteRecipe: HashSet<Int> = hashSetOf()
    )
}

