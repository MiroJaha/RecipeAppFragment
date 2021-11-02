package com.example.recipeappfragment.API

import com.example.recipeappfragment.Information
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {
    @GET("/recipes/")
    fun getInformation(): Call<List<Information>>

    @POST("/recipes/")
    fun addRecipe(@Body recipeData: Information): Call<Information>
}