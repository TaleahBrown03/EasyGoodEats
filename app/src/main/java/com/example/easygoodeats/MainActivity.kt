package com.example.easygoodeats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easygoodeats.ui.theme.EasyGoodEatsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyGoodEatsTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: RecipeViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedRecipeId by remember { mutableStateOf<Int?>(null) }

    if (selectedRecipeId != null) {
        RecipeDetailScreen(
            recipeId = selectedRecipeId!!,
            viewModel = viewModel,
            onBack = { selectedRecipeId = null }
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Search") },
                        label = { Text("Search") },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = "Saved") },
                        label = { Text("Saved") },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.DateRange, contentDescription = "Planner") },
                        label = { Text("Planner") },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") },
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 }
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (selectedTab) {
                    0 -> SearchScreen(onRecipeClick = { id -> selectedRecipeId = id })
                    1 -> SavedScreen(viewModel = viewModel, onRecipeClick = { id -> selectedRecipeId = id })
                    2 -> PlannerScreen(viewModel = viewModel, onRecipeClick = { id -> selectedRecipeId = id })
                    3 -> SettingsScreen()
                }
            }
        }
    }
}
