package com.example.easygoodeats.data.api

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("id")             val id: Int,
    @SerializedName("title")          val title: String,
    @SerializedName("image")          val image: String,
    @SerializedName("readyInMinutes") val readyInMinutes: Int,
    @SerializedName("servings")       val servings: Int = 0,
    @SerializedName("summary")        val summary: String = "",
    @SerializedName("instructions")   val instructions: String = "",
    @SerializedName("extendedIngredients") val ingredients: List<Ingredient> = emptyList()
)

data class Ingredient(
    @SerializedName("original") val original: String
)

data class RecipeSearchResponse(
    @SerializedName("results")      val results: List<Recipe>,
    @SerializedName("totalResults") val totalResults: Int
)