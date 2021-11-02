package com.example.recipeappfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.recipeappfragment.Room.RecipesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val connection= RecipesDatabase.getInstance(application).recipeDao()

    fun gettingAllRecipes(): LiveData<List<Information>>{
        return connection.gettingAllRecipes()
    }

    fun addNewRecipe(recipe: Information){
        CoroutineScope(IO).launch {
            connection.addNewRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: Information){
        CoroutineScope(IO).launch {
            connection.updateRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: Information){
        CoroutineScope(IO).launch {
            connection.deleteRecipe(recipe)
        }
    }
}