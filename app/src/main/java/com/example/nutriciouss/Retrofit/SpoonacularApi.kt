package com.example.nutriciouss.Retrofit

import com.example.nutriciouss.Data.RecipieResponse
import okhttp3.Response
import retrofit2.http.GET
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

}