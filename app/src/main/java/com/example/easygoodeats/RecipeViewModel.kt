package com.example.easygoodeats

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.easygoodeats.data.api.Recipe

data class PlannedMeal(
    val recipe: Recipe,
    val mealType: String // "Breakfast", "Lunch", "Dinner"
)

class RecipeViewModel : ViewModel() {
    // Saved Recipes
    private val _savedRecipes = mutableStateListOf<Recipe>()
    val savedRecipes: List<Recipe> get() = _savedRecipes

    fun toggleSave(recipe: Recipe) {
        if (_savedRecipes.any { it.id == recipe.id }) {
            _savedRecipes.removeAll { it.id == recipe.id }
        } else {
            _savedRecipes.add(recipe)
        }
    }

    fun isSaved(recipeId: Int): Boolean = _savedRecipes.any { it.id == recipeId }

    // Meal Plan: Day -> List of PlannedMeals
    private val _mealPlan = mutableMapOf<String, MutableList<PlannedMeal>>()
    val mealPlan: Map<String, List<PlannedMeal>> get() = _mealPlan

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val mealTypes = listOf("Breakfast", "Lunch", "Dinner")

    init {
        daysOfWeek.forEach { _mealPlan[it] = mutableStateListOf() }
    }

    fun addToMealPlan(day: String, mealType: String, recipe: Recipe) {
        _mealPlan[day]?.let { list ->
            // Allow same recipe in different meal types or days
            if (list.none { it.recipe.id == recipe.id && it.mealType == mealType }) {
                list.add(PlannedMeal(recipe, mealType))
            }
        }
    }

    fun removeFromMealPlan(day: String, recipeId: Int, mealType: String) {
        _mealPlan[day]?.removeAll { it.recipe.id == recipeId && it.mealType == mealType }
    }
}
