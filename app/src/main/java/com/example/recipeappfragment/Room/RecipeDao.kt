package com.example.recipeappfragment.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipeappfragment.Information

@Dao
interface RecipeDao {
    @Query("SELECT * FROM Recipes")
    fun gettingAllRecipes(): LiveData<List<Information>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewRecipe(recipe: Information)

    @Delete
    fun deleteRecipe(recipe: Information)

    @Update
    fun updateRecipe(recipe: Information)
}