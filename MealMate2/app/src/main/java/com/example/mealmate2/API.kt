package com.example.mealmate2

import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface MealDbApi {

    @GET("search.php")
    suspend fun searchMealsByName(@Query("s") name: String): Meals

    @GET("search.php")
    suspend fun getMealsByFirstLetter(@Query("f") letter: String): Meals

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): Meals

    @GET("random.php")
    suspend fun getRandomMeal(): Meals

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("list.php")
    suspend fun getAreas(@Query("a") list: String = "list"): AreaResponse

    @GET("filter.php")
    suspend fun filterByIngredient(@Query("i") ingredient: String): Meals

    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category: String): Meals

    @GET("filter.php")
    suspend fun filterByArea(@Query("a") area: String): Meals
}

object MealDbClient {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    // Create logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configure OkHttpClient with timeout and logging
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    // Create Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Create API service
    val api: MealDbApi by lazy {
        retrofit.create(MealDbApi::class.java)
    }
}

class MealRepository {
    private val api = MealDbClient.api

    suspend fun searchMealsByName(query: String): Result<List<Meal>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchMealsByName(query)
            Result.success(response.meals ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMealsByFirstLetter(letter: String): Result<List<Meal>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMealsByFirstLetter(letter)
            Result.success(response.meals ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMealById(id: String): Result<Meal?> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMealById(id)
            Result.success(response.meals?.firstOrNull())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRandomMeal(): Result<Meal?> = withContext(Dispatchers.IO) {
        try {
            val response = api.getRandomMeal()
            Result.success(response.meals?.firstOrNull())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getCategories()
            Result.success(response.categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAreas(): Result<List<AreaItem>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAreas()
            Result.success(response.meals)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun filterByIngredient(ingredient: String): Result<List<Meal>> = withContext(Dispatchers.IO) {
        try {
            val response = api.filterByIngredient(ingredient)
            Result.success(response.meals ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun filterByCategory(category: String): Result<List<Meal>> = withContext(Dispatchers.IO) {
        try {
            val response = api.filterByCategory(category)
            Result.success(response.meals ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun filterByArea(area: String): Result<List<Meal>> = withContext(Dispatchers.IO) {
        try {
            val response = api.filterByArea(area)
            Result.success(response.meals ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}