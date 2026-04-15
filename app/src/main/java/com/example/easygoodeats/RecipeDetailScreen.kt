package com.example.easygoodeats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.example.easygoodeats.api.RetrofitClient
import com.example.easygoodeats.data.api.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    viewModel: RecipeViewModel,
    onBack: () -> Unit
) {
    var recipe by remember { mutableStateOf<Recipe?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showMealPlanDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(recipeId) {
        isLoading = true
        errorMessage = null
        try {
            recipe = RetrofitClient.api.getRecipeById(
                id = recipeId,
                apiKey = "c840a5337e754780aeb63eca175591fd"
            )
        } catch (e: Exception) {
            errorMessage = e.message ?: "Unknown error occurred"
        } finally {
            isLoading = false
        }
    }

    if (showMealPlanDialog && recipe != null) {
        AlertDialog(
            onDismissRequest = {
                showMealPlanDialog = false
                selectedDay = null
            },
            title = { Text(if (selectedDay == null) "Add to Meal Plan" else "Select Meal Type") },
            text = {
                Column {
                    if (selectedDay == null) {
                        viewModel.daysOfWeek.forEach { day ->
                            TextButton(
                                onClick = { selectedDay = day },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(day)
                            }
                        }
                    } else {
                        viewModel.mealTypes.forEach { type ->
                            TextButton(
                                onClick = {
                                    viewModel.addToMealPlan(selectedDay!!, type, recipe!!)
                                    showMealPlanDialog = false
                                    selectedDay = null
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(type)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showMealPlanDialog = false
                    selectedDay = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.title ?: "Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    recipe?.let { r ->
                        val isSaved = viewModel.isSaved(r.id)
                        IconButton(onClick = { viewModel.toggleSave(r) }) {
                            Icon(
                                imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Save",
                                tint = if (isSaved) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (recipe != null) {
                ExtendedFloatingActionButton(
                    onClick = { showMealPlanDialog = true },
                    icon = { Icon(Icons.Filled.Favorite, null) },
                    text = { Text("Add to Planner") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage != null) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Failed to load recipe", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBack) {
                        Text("Go Back")
                    }
                }
            } else {
                recipe?.let { r ->
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            AsyncImage(
                                model = r.image,
                                contentDescription = r.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = r.title,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Text("⏱ ${r.readyInMinutes} min",
                                        style = MaterialTheme.typography.bodyMedium)
                                    Text("🍽 ${r.servings} servings",
                                        style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                        if (r.ingredients.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Ingredients",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            items(r.ingredients) { ingredient ->
                                Text(
                                    text = "• ${ingredient.original}",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        if (r.instructions.isNotBlank()) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Instructions",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = HtmlCompat.fromHtml(
                                        r.instructions,
                                        HtmlCompat.FROM_HTML_MODE_LEGACY
                                    ).toString(),
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(80.dp)) // Extra space for FAB
                            }
                        }
                    }
                }
            }
        }
    }
}
