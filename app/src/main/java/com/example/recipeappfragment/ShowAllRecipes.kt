package com.example.recipeappfragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ShowAllRecipes : Fragment() {

    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private lateinit var addButton: Button
    private lateinit var rvInformation: RecyclerView
    private lateinit var informationList : ArrayList<Information>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_show_all_recipes, container, false)

        addButton = view.findViewById(R.id.addButton)
        rvInformation = view.findViewById(R.id.rvInformation)
        informationList = arrayListOf()

        addButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_showAllRecipes_to_addNewRecipe)
        }
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait")

        val adapter = RVAdapter(informationList)
        rvInformation.adapter = adapter
        rvInformation.layoutManager = LinearLayoutManager(context)
        adapter.setOnItemClickListener(object : RVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle= Bundle()
                bundle.putInt("pk", informationList[position].pk)
                bundle.putString("title", informationList[position].title)
                bundle.putString("author", informationList[position].author)
                bundle.putString("ingredients", informationList[position].ingredients)
                bundle.putString("instructions", informationList[position].instructions)
                Navigation.findNavController(view).navigate(R.id.action_showAllRecipes_to_editRecipe,bundle)
            }
        })

        mainViewModel.gettingAllRecipes().observe(viewLifecycleOwner){
                data ->
            progressDialog.show()
            informationList.clear()
            informationList.addAll(data)
            adapter.notifyDataSetChanged()
            progressDialog.dismiss()
        }

        return view
    }

}