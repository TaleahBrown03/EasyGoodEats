package com.example.easygoodeats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.easygoodeats.api.RetrofitClient
import com.example.easygoodeats.data.api.Recipe
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onRecipeClick: (Int) -> Unit) {
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var recipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }

    // Filter states
    var selectedDiet by remember { mutableStateOf<String?>(null) }
    var selectedCuisine by remember { mutableStateOf<String?>(null) }
    var selectedType by remember { mutableStateOf<String?>(null) }
    var maxTime by remember { mutableFloatStateOf(60f) }

    val diets = listOf("Gluten Free", "Ketogenic", "Vegetarian", "Vegan", "Pescetarian", "Paleo")
    val cuisines = listOf("Italian", "Mexican", "Chinese", "Indian", "Japanese", "French", "American")
    val mealTypes = listOf("breakfast", "lunch", "dinner", "main course", "side dish", "dessert")

    val cookingImages = listOf(
        "https://images.unsplash.com/photo-1556910103-1c02745aae4d?auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1547592166-23ac45744acd?auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=500&q=60"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Easy Good Eats",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showFilters = !showFilters }) {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        contentDescription = "Filters",
                        tint = if (showFilters) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    )
                }
            }
        }

        if (recipes.isEmpty() && !isLoading) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(cookingImages) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = "Cooking",
                            modifier = Modifier
                                .size(240.dp, 150.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search ingredients or dish...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true
            )
        }

        item {
            AnimatedVisibility(visible = showFilters) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    FilterSection(title = "Diet", options = diets, selected = selectedDiet, onSelected = { selectedDiet = if (selectedDiet == it) null else it })
                    FilterSection(title = "Cuisine", options = cuisines, selected = selectedCuisine, onSelected = { selectedCuisine = if (selectedCuisine == it) null else it })
                    FilterSection(title = "Meal Type", options = mealTypes, selected = selectedType, onSelected = { selectedType = if (selectedType == it) null else it })

                    Text("Max Time: ${maxTime.toInt()} min", style = MaterialTheme.typography.labelLarge)
                    Slider(
                        value = maxTime,
                        onValueChange = { maxTime = it },
                        valueRange = 10f..180f,
                        steps = 17
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    isLoading = true
                    errorMessage = ""
                    scope.launch {
                        try {
                            val response = RetrofitClient.api.searchRecipes(
                                query = query,
                                diet = selectedDiet?.lowercase(),
                                cuisine = selectedCuisine?.lowercase(),
                                type = selectedType?.lowercase(),
                                maxReadyTime = maxTime.toInt(),
                                apiKey = "c840a5337e754780aeb63eca175591fd"
                            )
                            recipes = response.results
                            isLoading = false
                            if (recipes.isEmpty()) errorMessage = "No recipes found."
                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Find Recipes")
            }
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        if (errorMessage.isNotBlank()) {
            item {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }
        }

        items(recipes) { recipe ->
            RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selected: String?,
    onSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = selected == option,
                    onClick = { onSelected(option) },
                    label = { Text(option) }
                )
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Text(
                    text = "Ready in ${recipe.readyInMinutes} min",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
