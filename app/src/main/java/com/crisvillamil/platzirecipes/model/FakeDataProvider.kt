package com.crisvillamil.platzirecipes.model

object FakeDataProvider {

    val fakeRemoteData by lazy {
        ArrayList<Recipe>().apply {
            repeat(30) {
                add(
                    Recipe(
                        recipeId = it + 1,
                        name = "Receta ${it + 1}",
                        imageUrl = arrayListOf(
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/Bandepaisabog.JPG/546px-Bandepaisabog.JPG",
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

    val fakeLocalPreferences by lazy { FakeLocalPreferences() }

    data class FakeLocalPreferences(
        val userRatedRecipes: HashMap<Int, Int> = hashMapOf(),
        val userFavoriteRecipe: HashSet<Recipe> = hashSetOf()
    )
}