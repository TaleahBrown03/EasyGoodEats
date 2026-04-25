package com.example.easygoodeats

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    
    // Sound Generator
    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100) }

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
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
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
                                    // Play sound feedback
                                    toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 200)
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
                        IconButton(onClick = { 
                            viewModel.toggleSave(r)
                            // Play sound feedback
                            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                        }) {
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = r.title,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    
                                    // CANVAS COMPONENT: Simple decorative star/shape
                                    Canvas(modifier = Modifier.size(40.dp).padding(4.dp)) {
                                        val path = Path().apply {
                                            moveTo(size.width / 2, 0f)
                                            lineTo(size.width * 0.6f, size.height * 0.4f)
                                            lineTo(size.width, size.height * 0.4f)
                                            lineTo(size.width * 0.7f, size.height * 0.6f)
                                            lineTo(size.width * 0.8f, size.height)
                                            lineTo(size.width / 2, size.height * 0.8f)
                                            lineTo(size.width * 0.2f, size.height)
                                            lineTo(size.width * 0.3f, size.height * 0.6f)
                                            lineTo(0f, size.height * 0.4f)
                                            lineTo(size.width * 0.4f, size.height * 0.4f)
                                            close()
                                        }
                                        drawPath(path, color = Color(0xFFFFD700), style = Fill)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Text("⏱ ${r.readyInMinutes} min",
                                        style = MaterialTheme.typography.bodyMedium)
                                    Text("🍽 ${r.servings} servings",
                                        style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }

                        // Nutrition Section
                        r.nutrition?.let { nutrition ->
                            item {
                                Text(
                                    text = "Nutrition Per Serving",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val mainNutrients = listOf("Calories", "Fat", "Protein", "Carbohydrates")
                                    val filtered = nutrition.nutrients.filter { it.name in mainNutrients }
                                    items(filtered) { nutrient ->
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "${nutrient.amount.toInt()}${nutrient.unit}",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = nutrient.name,
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
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
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = ingredient.original,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        // Equipment Section
                        val allEquipment = r.analyzedInstructions.flatMap { it.steps }.flatMap { it.equipment }.distinctBy { it.name }
                        if (allEquipment.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Equipment Needed",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(allEquipment) { equipment ->
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Box(
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                AsyncImage(
                                                    model = "https://spoonacular.com/cdn/equipment_100x100/${equipment.image}",
                                                    contentDescription = equipment.name,
                                                    modifier = Modifier.size(45.dp)
                                                )
                                            }
                                            Text(
                                                text = equipment.name,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.widthIn(max = 70.dp),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Analyzed Instructions Section
                        if (r.analyzedInstructions.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Instructions",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            r.analyzedInstructions.forEach { instruction ->
                                if (instruction.name.isNotBlank()) {
                                    item {
                                        Text(
                                            text = instruction.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                                items(instruction.steps) { step ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 6.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                    ) {
                                        Row(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = "${step.number}",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.width(30.dp)
                                            )
                                            Text(
                                                text = step.step,
                                                style = MaterialTheme.typography.bodyMedium,
                                                lineHeight = 20.sp
                                            )
                                        }
                                    }
                                }
                            }
                        } else if (r.instructions.isNotBlank()) {
                            // Fallback to HTML instructions if analyzed ones are missing
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
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(100.dp)) // Extra space for FAB
                        }
                    }
                }
            }
        }
    }
}
