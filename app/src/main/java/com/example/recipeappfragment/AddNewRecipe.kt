package com.example.recipeappfragment

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.recipeappfragment.API.APIClient
import com.example.recipeappfragment.API.APIInterface
import io.github.muddz.styleabletoast.StyleableToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewRecipe : Fragment() {

    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private lateinit var saveButton: Button
    private lateinit var viewButton: Button
    private lateinit var callData: Button
    private lateinit var titleEntry: EditText
    private lateinit var authorEntry: EditText
    private lateinit var ingredientsEntry: EditText
    private lateinit var instructionsEntry: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_add_new_recipe, container, false)

        saveButton= view.findViewById(R.id.saveButton)
        viewButton= view.findViewById(R.id.viewButton)
        callData= view.findViewById(R.id.callData)
        titleEntry= view.findViewById(R.id.titleEntry)
        authorEntry= view.findViewById(R.id.authorEntry)
        ingredientsEntry= view.findViewById(R.id.ingredientsEntry)
        instructionsEntry= view.findViewById(R.id.instructionsEntry)

        viewButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_addNewRecipe_to_showAllRecipes)
        }

        callData.setOnClickListener{
            callFromApi()
        }

        saveButton.setOnClickListener{
            if (checkEntry()){
                addNewRecipe()
                titleEntry.text.clear()
                authorEntry.text.clear()
                ingredientsEntry.text.clear()
                instructionsEntry.text.clear()
                val keyboard: View?= requireActivity().currentFocus
                if (keyboard != null) {
                    val inputMethodManager: InputMethodManager =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(keyboard.windowToken, 0)
                }

            }
            else{
                StyleableToast.makeText(requireContext(),"Please Enter Correct Values",R.style.mytoast).show()
            }
        }

        return view
    }

    private fun callFromApi() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        apiInterface?.getInformation()?.enqueue(object : Callback<List<Information>> {
            override fun onResponse(call: Call<List<Information>>, response: Response<List<Information>>) {
                try {
                    for (recipe in response.body()!!) {
                        mainViewModel.addNewRecipe(
                            Information(
                                recipe.pk,
                                recipe.title!!,
                                recipe.author!!,
                                recipe.ingredients!!,
                                recipe.instructions!!
                            )
                        )
                    }
                    StyleableToast.makeText(this@AddNewRecipe.requireContext(), "Saved Successfully!!", R.style.mytoast)
                        .show()
                    progressDialog.dismiss()
                } catch (e: Exception) {
                    Log.d("MyInformation", "failed")
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<List<Information>>, t: Throwable) {
                StyleableToast.makeText(this@AddNewRecipe.requireContext(), "Failed ", R.style.mytoast)
                    .show()
                progressDialog.dismiss()
            }
        })
    }

    private fun checkEntry() : Boolean{
        return when {
            titleEntry.text.isBlank() -> false
            authorEntry.text.isBlank() -> false
            ingredientsEntry.text.isBlank() -> false
            else -> instructionsEntry.text.isNotBlank()
        }
    }

    private fun addNewRecipe(){
        mainViewModel.addNewRecipe(
            Information(
                0,
                titleEntry.text.toString(),
                authorEntry.text.toString(),
                ingredientsEntry.text.toString(),
                instructionsEntry.text.toString()
            )
        )
        StyleableToast.makeText(requireContext(), "Saved Successfully!!", R.style.mytoast)
            .show()
    }

}