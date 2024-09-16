package com.example.recipes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project.utils.AnimationHelper
import com.example.recipes.data.ListAlarm
import com.example.recipes.databinding.FragmentLaterBinding
import com.example.recipes.rv_adapter.RecipesListRecyclerAdapter


class LaterFragment : Fragment() {
    private lateinit var binding: FragmentLaterBinding
    private lateinit var recipesAdapter: RecipesListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(binding.watchLater, requireActivity(), 3)
        ListAlarm.updateList()
        val listAlarm = ListAlarm.gatListAlarm()
        var str = ""
        listAlarm.forEach{
            str += it.name
            str += " "
            str += it.date.getTime().toString()
            str += "\n"
        }
        binding.listRecipes.text = str
    }
}