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
    @SerializedName("extendedIngredients") val ingredients: List<Ingredient> = emptyList(),
    @SerializedName("analyzedInstructions") val analyzedInstructions: List<AnalyzedInstruction> = emptyList(),
    @SerializedName("nutrition") val nutrition: Nutrition? = null
)

data class Ingredient(
    @SerializedName("original") val original: String
)

data class AnalyzedInstruction(
    @SerializedName("name") val name: String,
    @SerializedName("steps") val steps: List<Step>
)

data class Step(
    @SerializedName("number") val number: Int,
    @SerializedName("step") val step: String,
    @SerializedName("equipment") val equipment: List<Equipment> = emptyList()
)

data class Equipment(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
)

data class Nutrition(
    @SerializedName("nutrients") val nutrients: List<Nutrient>
)

data class Nutrient(
    @SerializedName("name") val name: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("unit") val unit: String
)

data class RecipeSearchResponse(
    @SerializedName("results")      val results: List<Recipe>,
    @SerializedName("totalResults") val totalResults: Int
)
