package com.example.recipes.view.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipes.MainActivity
import com.example.recipes.databinding.FragmentSearchBinding
import com.example.recipes.viewmodel.SearchFragmentViewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SearchFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.getTrivia()
        binding.trivia.movementMethod = ScrollingMovementMethod()
        val s = viewModel.interactor.getTrivia()
        binding.trivia.text = s


        binding.btnFind.setOnClickListener {
            (requireActivity() as MainActivity).paramsSearch.cuisine = binding.cuisineList.getSelectedItem().toString()
            (requireActivity() as MainActivity).paramsSearch.diet = binding.dietList.getSelectedItem().toString()
            (requireActivity() as MainActivity).paramsSearch.ingredients = binding.ingredients.text.toString()
            (requireActivity() as MainActivity).paramsSearch.type = binding.typeList.getSelectedItem().toString()
            (requireActivity() as MainActivity).paramsSearch.time = binding.timeList.getSelectedItem().toString()
           // val data = DataSearch(cuisine, diet, ingredients, type, time)
           // viewModel.interactor.setParam(data)
            (requireActivity() as MainActivity).paramsSearch.cuisine
            (requireActivity() as MainActivity).launchResultFragment()
        }


    }
}