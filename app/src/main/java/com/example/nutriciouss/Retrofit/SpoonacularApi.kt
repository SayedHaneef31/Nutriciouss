package com.example.nutriciouss.Retrofit

import com.example.nutriciouss.Data.Meal
import com.example.nutriciouss.Data.MealPlanResponse
import com.example.nutriciouss.Data.Recipie
import com.example.nutriciouss.Data.RecipieResponse
import com.example.nutriciouss.Data.RecipieSuggestion
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi
{
    //Querry ke andar jo parameter de rahe hai wo important hai...keep it similar to api documnetation
    @GET("recipes/random")
    suspend fun getRandomRecipie(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int=1,
        @Query("include-tags") tags: String="whole30,indian,protein"

    ): RecipieResponse

    @GET("recipes/autocomplete")
    suspend fun autoCompleteSearch(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String,
        @Query("number") number: Int=6
    ) : List<RecipieSuggestion>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String,
        @Query("includeNutrition") includeNutrition: Boolean=true
    ) : Recipie


    @GET("mealplanner/generate")
    suspend fun generateMealPlan(
        @Query("timeFrame") timeFrame: String = "day",
        @Query("targetCalories") targetCalories: Int,
        @Query("diet") diet: String,
        @Query("apiKey") apiKey: String
    ): MealPlanResponse
}