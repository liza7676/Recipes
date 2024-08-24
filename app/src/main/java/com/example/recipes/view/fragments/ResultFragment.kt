package com.example.recipes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipes.databinding.FragmentResultBinding
import com.example.recipes.viewmodel.ResultFragmentViewModel


class ResultFragment(param:DataSearch) : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(ResultFragmentViewModel::class.java)
    }
    private var params = param
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.text.text = params.cuisine
        val s = viewModel.interactor.getRecipes()
    }

}