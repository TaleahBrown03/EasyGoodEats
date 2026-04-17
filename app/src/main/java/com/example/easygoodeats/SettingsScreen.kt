package com.example.easygoodeats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: RecipeViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsHeader("Preferences")
            SettingsClickableItem(
                title = "Dark Mode",
                icon = Icons.Default.Build,
                trailing = {
                    Switch(
                        checked = viewModel.isDarkMode,
                        onCheckedChange = { viewModel.isDarkMode = it }
                    )
                }
            )
            SettingsClickableItem(
                title = "Use Metric System",
                subtitle = "Grams and Milliliters instead of Cups/Oz",
                icon = Icons.Default.Info,
                trailing = {
                    Switch(
                        checked = viewModel.useMetricSystem,
                        onCheckedChange = { viewModel.useMetricSystem = it }
                    )
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            SettingsHeader("Account & Data")
            SettingsClickableItem(
                title = "Clear Saved Recipes",
                icon = Icons.Default.Favorite,
                onClick = { viewModel.clearSavedRecipes() }
            )
            SettingsClickableItem(
                title = "Reset Weekly Planner",
                icon = Icons.Default.DateRange,
                onClick = { viewModel.resetMealPlan() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsHeader("About")
            SettingsClickableItem(
                title = "Version",
                subtitle = "0.3.0 (Alpha)",
                icon = Icons.Default.Info,
                onClick = {}
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // API Attribution
            Text(
                text = "Powered by Spoonacular API",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun SettingsClickableItem(
    title: String,
    icon: ImageVector,
    subtitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = trailing,
        modifier = if (onClick != null) Modifier.fillMaxWidth().clickable { onClick() } else Modifier
    )
}
