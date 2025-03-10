package com.example.mealmate2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = MealRepository()

    private val _randomMeal = MutableStateFlow<Meal?>(null)
    val randomMeal: StateFlow<Meal?> = _randomMeal.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _areas = MutableStateFlow<List<AreaItem>>(emptyList())
    val areas: StateFlow<List<AreaItem>> = _areas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchRandomMeal()
        fetchCategories()
        fetchAreas()
    }

    fun fetchRandomMeal() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _randomMeal.value = repository.getRandomMeal().getOrNull()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load random meal: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _categories.value = repository.getCategories().getOrElse { emptyList() }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load categories: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchAreas() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _areas.value = repository.getAreas().getOrElse { emptyList() }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load areas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class SearchViewModel : ViewModel() {
    private val repository = MealRepository()

    private val _searchResults = MutableStateFlow<List<Meal>>(emptyList())
    val searchResults: StateFlow<List<Meal>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun searchMeals(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _searchResults.value = repository.searchMealsByName(query).getOrElse { emptyList() }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Search failed: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _searchResults.value = repository.filterByCategory(category).getOrElse { emptyList() }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Filter failed: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByArea(area: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _searchResults.value = repository.filterByArea(area).getOrElse { emptyList() }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Filter failed: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByIngredient(ingredient: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _searchResults.value = repository.filterByIngredient(ingredient).getOrElse { emptyList() }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Filter failed: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class MealDetailViewModel : ViewModel() {
    private val repository = MealRepository()

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> = _meal.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadMealDetails(mealId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _meal.value = repository.getMealById(mealId).getOrNull()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load meal details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class FavoritesViewModel : ViewModel() {
    private val _favorites = MutableStateFlow<List<Meal>>(emptyList())
    val favorites: StateFlow<List<Meal>> = _favorites.asStateFlow()

    fun addFavorite(meal: Meal) {
        _favorites.value += meal
    }

    fun removeFavorite(meal: Meal) {
        _favorites.value -= meal
    }
}