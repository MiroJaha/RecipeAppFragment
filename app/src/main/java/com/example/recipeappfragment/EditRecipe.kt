package com.example.recipeappfragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.muddz.styleabletoast.StyleableToast


class EditRecipe : Fragment() {

    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private lateinit var titleTV: EditText
    private lateinit var authorTV: EditText
    private lateinit var ingredientsTV: EditText
    private lateinit var instructionsTV: EditText
    private lateinit var backButton: FloatingActionButton
    private lateinit var editButton: FloatingActionButton
    private lateinit var deleteButton: FloatingActionButton
    private var pk= 0
    private lateinit var title: String
    private lateinit var author: String
    private lateinit var ingredients: String
    private lateinit var instructions: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_edit_recipe, container, false)

        titleTV= view.findViewById(R.id.titleTV)
        authorTV= view.findViewById(R.id.authorTV)
        ingredientsTV= view.findViewById(R.id.ingredientsTv)
        instructionsTV= view.findViewById(R.id.instructionsTV)
        backButton= view.findViewById(R.id.backButton)
        editButton= view.findViewById(R.id.editButton)
        deleteButton= view.findViewById(R.id.deleteButton)

        pk= arguments?.getInt("pk")!!
        title= arguments?.getString("title")!!
        author= arguments?.getString("author")!!
        ingredients= arguments?.getString("ingredients")!!
        instructions= arguments?.getString("instructions")!!

        setHint()

        editButton.setOnClickListener{
            if (checkEntry()){
                mainViewModel.updateRecipe(Information(pk, title, author, ingredients, instructions))
                StyleableToast.makeText(
                    requireContext(),
                    "Updated Successfully!!",
                    R.style.mytoast
                ).show()
                clearEntry()
                setHint()
            }
            else
                StyleableToast.makeText(
                    requireContext(),
                    "Please Enter Valid Values!!",
                    R.style.mytoast
                ).show()
        }
        deleteButton.setOnClickListener{
            AlertDialog.Builder(context)
                .setTitle("Are You Sure You Want To Delete This Recipe")
                .setCancelable(false)
                .setPositiveButton("YES"){_,_ ->
                    mainViewModel.deleteRecipe(Information(pk, title, author, ingredients, instructions))
                    StyleableToast.makeText(
                        requireContext(),
                        "Deleted Success!!",
                        R.style.mytoast
                    ).show()
                    Navigation.findNavController(view).navigate(R.id.action_editRecipe_to_showAllRecipes)
                }
                .setNegativeButton("Cancel"){dialog,_ -> dialog.cancel() }
                .show()
        }
        backButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_editRecipe_to_showAllRecipes)
        }

        return view
    }

    private fun clearEntry() {
        titleTV.text.clear()
        titleTV.clearFocus()
        authorTV.text.clear()
        authorTV.clearFocus()
        ingredientsTV.text.clear()
        ingredientsTV.clearFocus()
        instructionsTV.text.clear()
        instructionsTV.clearFocus()
    }

    private fun checkEntry(): Boolean {
        var check= false
        if (titleTV.text.isNotBlank()) {
            title= titleTV.text.toString()
            check= true
        }
        if (authorTV.text.isNotBlank()) {
            author= authorTV.text.toString()
            check= true
        }
        if (ingredientsTV.text.isNotBlank()) {
            ingredients= ingredientsTV.text.toString()
            check= true
        }
        if (instructionsTV.text.isNotBlank()) {
            instructions= instructionsTV.text.toString()
            check= true
        }
        return check
    }

    private fun setHint() {
        titleTV.hint= "Recipe Name: $title"
        authorTV.hint= "Author Name: $author"
        ingredientsTV.hint= "Ingredients:\n\n$ingredients"
        instructionsTV.hint= "Instructions:\n\n$instructions"
    }
}