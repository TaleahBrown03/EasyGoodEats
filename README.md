# Easy Good Eats - Project Outline

## 1. Project Description
**Easy Good Eats** is a comprehensive Android application designed to simplify the cooking and meal-planning experience. It leverages the **Spoonacular API** to provide users with access to thousands of recipes, detailed nutritional information, and step-by-step cooking instructions. The app aims to be a one-stop-shop for home cooks of all skill levels, offering personalized recipe discovery and organizational tools.

## 2. Problem Addressing
Many individuals struggle with "decision fatigue" when it comes to daily meals, or they find it difficult to organize recipes and plan their weekly nutrition. **Easy Good Eats** addresses:
* **Inefficient Recipe Searching:** Provides powerful filters (diet, cuisine, time) to find exactly what's needed.
* **Lack of Organization:** Offers a "Saved Recipes" feature to keep track of favorites.
* **Poor Meal Planning:** Includes a dedicated Weekly Planner to assign recipes to specific days and meal types (Breakfast, Lunch, Dinner).
* **Information Overload:** Presents recipe data in a clean, readable format with essential details like cooking time and health scores.

## 3. Platform
* **Operating System:** Android
* **Minimum SDK:** API 24 (Android 7.0)
* **Target SDK:** API 36
* **UI Framework:** Jetpack Compose (Modern Declarative UI)

## 4. Front/Back End Support
* **Frontend:** Built entirely using **Kotlin** and **Jetpack Compose**. It utilizes Material Design 3 components for a modern look and feel.
* **State Management:** Implements **ViewModel** and **mutableState** for reactive UI updates and data persistence during the app session.
* **Networking (Backend API):** Uses **Retrofit** and **Gson** to communicate with the **Spoonacular REST API**.
* **Image Loading:** Uses **Coil** for efficient, asynchronous image loading and caching from web URLs.

## 5. Functionality
* **Search & Discover:** Search by ingredients or dish name with advanced filtering for diets (Vegan, Keto, etc.), cuisines, and max preparation time.
* **Recipe Details:** View comprehensive information including images, ingredients, detailed instructions, and ready-in-time.
* **Favorites System:** "Heart" recipes to save them to a persistent list for quick access later.
* **Weekly Meal Planner:** Assign recipes to any day of the week and categorize them by meal type.
* **App Settings:** Customization options including Dark Mode toggle and measurement system preferences (Metric vs. Imperial).

## 6. Design (Wireframes)
The app follows a structured 4-tab navigation layout:
* **Search Tab:** The landing screen featuring a search bar, quick filters, and recipe result cards.
* **Saved Tab:** A dedicated space for recipes the user has favorited.
* **Planner Tab:** A calendar-style view of the week where users can manage their upcoming meals.
* **Settings Tab:** A clean list of preferences and account data management tools.

---
*Developed for the Spring 2026 Term.*
