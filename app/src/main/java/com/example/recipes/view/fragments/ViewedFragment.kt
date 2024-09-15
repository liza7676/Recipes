package com.example.recipes.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.utils.AnimationHelper
import com.example.recipes.MainActivity
import com.example.recipes.data.entity.Recipes
import com.example.recipes.databinding.FragmentViewedBinding
import com.example.recipes.rv_adapter.RecipesListRecyclerAdapter
import com.example.recipes.utils.AutoDisposable
import com.example.recipes.utils.addTo
import com.example.recipes.viewmodel.ViewedFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class ViewedFragment : Fragment() {
    private lateinit var binding: FragmentViewedBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(ViewedFragmentViewModel::class.java)
    }
    private val autoDisposable = AutoDisposable()
    private lateinit var recipesAdapter: RecipesListRecyclerAdapter
    private var recipesDataBase = listOf<Recipes>()
        get() = field
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение, то кладем его в переменную
            field = value
            //Обновляем RV адаптер
            recipesAdapter.addItems(field)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoDisposable.bindTo(lifecycle)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewedBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            //оставим его пока пустым, он нам понадобится во второй части задания
            recipesAdapter =
                RecipesListRecyclerAdapter(object : RecipesListRecyclerAdapter.OnItemClickListener {
                    override fun click(recipes: Recipes) {
                        (requireActivity() as MainActivity).launchDetailsFragment(recipes)
                    }
                })
            //Присваиваем адаптер
            adapter = recipesAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())

        }
        initPullToRefresh()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.homeFragmentRoot, requireActivity(), 1)

        viewModel.recipesListData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                recipesAdapter.addItems(list)
                recipesDataBase = list
            }
            .addTo(autoDisposable)
        viewModel.showProgressBar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.progressBar.isVisible = it
            }
            .addTo(autoDisposable)
    }
    private fun initPullToRefresh() {
        //Вешаем слушатель, чтобы вызвался pull to refresh
        binding.pullToRefresh.setOnRefreshListener {
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            recipesAdapter.items.clear()
            viewModel.recipesListData
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    recipesAdapter.addItems(list)
                    recipesDataBase = list
                }
                .addTo(autoDisposable)
            //Убираем крутящиеся колечко
            binding.pullToRefresh.isRefreshing = false
        }
    }
}