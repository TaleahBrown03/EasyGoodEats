package com.example.easygoodeats.api

import com.example.easygoodeats.data.api.Recipe
import com.example.easygoodeats.data.api.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query")                query: String,
        @Query("diet")                 diet: String? = null,
        @Query("cuisine")              cuisine: String? = null,
        @Query("type")                 type: String? = null,
        @Query("maxReadyTime")         maxReadyTime: Int? = null,
        @Query("number")               number: Int = 20,
        @Query("addRecipeInformation") addInfo: Boolean = true,
        @Query("addRecipeNutrition")   addNutrition: Boolean = true,
        @Query("apiKey")               apiKey: String
    ): RecipeSearchResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id")      id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): Recipe
}
