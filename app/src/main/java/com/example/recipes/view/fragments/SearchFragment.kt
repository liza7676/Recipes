package com.example.recipes.view.fragments

import android.os.Bundle
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
        val s = viewModel.interactor.getTrivia()
        binding.trivia.text = s
        binding.btnFind.setOnClickListener {
            (requireActivity() as MainActivity).launchResultFragment()
            //viewModel.getTrivia()
        }
    }
}