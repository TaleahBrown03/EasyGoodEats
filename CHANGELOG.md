# Changelog

All notable changes to the **Easy Good Eats** project will be documented in this file.

## [1.0.0] - 2026-04-24
### Fixed
- **SettingsScreen:** Fixed "Unresolved reference 'clickable'" error by adding missing `androidx.compose.foundation.clickable` import.
- **SearchScreen:** Resolved critical app crash (`NoSuchMethodError: FlowRow`) by migrating from `FlowRow` to `LazyRow` for filter chips.
- **Stability:** Improved UI thread performance by optimizing lazy layout implementations.

### Changed
- **UI Layout:** Relocated search filter section below the "Find Recipes" button for improved user experience and accessibility.
- **Filter UI:** Updated diet, cuisine, and meal type selectors to use a horizontally scrollable `LazyRow` layout.

### Added
- Created initial `README.md` with project description, functionality, and platform details.
- Added `ENVIRONMENT_SETUP.md` (Wiki) with instructions for setting up the development environment and VM.

## [0.5.0] - 2026-04-15
### Added
- **Settings System:** Integrated application-wide preferences for Dark Mode and Metric/Imperial units.
- **Data Persistence Logic:** Enhanced `RecipeViewModel` to manage user-specific application states and preferences.

## [0.4.0] - 2026-04-08
### Added
- **Weekly Meal Planner:** Created a calendar-based interface for assigning recipes to specific days of the week.
- **Meal Categorization:** Added logic to categorize planned meals into Breakfast, Lunch, and Dinner.
- **Saved Recipes:** Implemented a favorites system allowing users to bookmark recipes locally for quick access.

## [0.3.0] - 2026-04-01
### Added
- **Search Functionality:** Integrated keyword search for ingredients and dish names.
- **Advanced Filtering:** Added the ability to filter results by Diet, Cuisine, and Max Preparation Time.
- **Recipe Detail Screen:** Developed a comprehensive view for ingredients, preparation steps, and nutritional summaries.

## [0.2.0] - 2026-03-25
### Added
- **API Integration:** Configured **Retrofit** and **Gson** to consume the Spoonacular REST API.
- **Data Models:** Established core data structures for Recipes, Ingredients, and Search Results.
- **Networking Layer:** Implemented `RetrofitClient` for centralized API management.

## [0.1.0] - 2026-03-24
### Added
- **Project Initialization:** Established Android project structure using **Kotlin** and **Jetpack Compose**.
- **Theming:** Configured Material Design 3 theme and color palette.
- **Navigation:** Implemented base `Scaffold` with Bottom Navigation Bar for the four core tabs.
