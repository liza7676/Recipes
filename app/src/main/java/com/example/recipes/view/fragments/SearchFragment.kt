package com.example.recipes.view.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipes.MainActivity
import com.example.recipes.R
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
        val gParams = viewModel.interactor.getParamsToPreferences()
        val cuisineList = getResources().getStringArray(R.array.cuisineList)
        for (i in 0 until cuisineList.size){
            if (cuisineList[i].equals(gParams.cuisine))
                binding.cuisineList.setSelection(i)
        }
        val dietList = getResources().getStringArray(R.array.dietList)
        for (i in 0 until dietList.size){
            if (dietList[i].equals(gParams.diet))
                binding.dietList.setSelection(i)
        }

        if(gParams.ingredients != null) {
            binding.ingredients.setText(gParams.ingredients)
        }
        val typeList = getResources().getStringArray(R.array.typeList)
        for (i in 0 until typeList.size){
            if (typeList[i].equals(gParams.type))
                binding.typeList.setSelection(i)
        }
        val timeList = getResources().getStringArray(R.array.timeList)
        for (i in 0 until timeList.size){
            if (timeList[i].equals(gParams.time))
                binding.timeList.setSelection(i)
        }
        binding.btnFind.setOnClickListener {
            //viewModel.interactor.clearCache()
            val params = DataSearch(
            binding.cuisineList.getSelectedItem().toString(),
            binding.dietList.getSelectedItem().toString(),
            binding.ingredients.text.toString(),
            binding.typeList.getSelectedItem().toString(),
            binding.timeList.getSelectedItem().toString())
            viewModel.interactor.saveParamsToPreferences(params)
            (requireActivity() as MainActivity).launchResultFragment()
        }


    }
}