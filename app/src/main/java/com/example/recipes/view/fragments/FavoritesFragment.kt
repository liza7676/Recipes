package com.example.recipes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.utils.AnimationHelper
import com.example.recipes.MainActivity
import com.example.recipes.R
import com.example.recipes.data.entity.Recipes
import com.example.recipes.databinding.FragmentFavoritesBinding
import com.example.recipes.rv_adapter.RecipesListRecyclerAdapter
import com.example.recipes.utils.AutoDisposable
import com.example.recipes.utils.addTo
import com.example.recipes.viewmodel.FavoritesFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale
import java.util.concurrent.TimeUnit


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(FavoritesFragmentViewModel::class.java)
    }
    private val autoDisposable = AutoDisposable()
    private lateinit var recipesAdapter: RecipesListRecyclerAdapter
    private var strSearch = ""
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
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }
        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            //Вешаем слушатель на клавиатуру
            binding.searchView.setOnQueryTextListener(object :
            //Вызывается на ввод символов
                SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    strSearch = newText
                    recipesAdapter.items.clear()
                    subscriber.onNext(newText)
                    return false
                }
                //Вызывается по нажатию кнопки "Поиск"
                override fun onQueryTextSubmit(query: String): Boolean {
                    strSearch = query
                    subscriber.onNext(query)
                    return false
                }
            })
        })
            .subscribeOn(Schedulers.io())
            .map {
                it.toLowerCase(Locale.getDefault()).trim()
            }
            .debounce(800, TimeUnit.MILLISECONDS)
            .filter {
                if (it.isEmpty()) {
                    viewModel.recipesListData
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { list ->
                            recipesAdapter.addItems(list)
                            recipesDataBase = list
                        }
                        .addTo(autoDisposable)
                }
                it.isNotBlank()
            }
            .flatMap { str ->
                viewModel.recipesListData
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                },
                onNext = {
                    if (!strSearch.isEmpty()) {
                        val newList = mutableListOf<Recipes>()
                        it.forEach() { recipes ->
                            if (recipes.title.toLowerCase().contains(strSearch.toLowerCase())){
                                newList.add(recipes)
                            }
                        }
                        recipesAdapter.addItems(newList)
                    } else {
                        recipesAdapter.addItems(it)
                    }
                }
            )
            .addTo(autoDisposable)

        initPullToRefresh()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.homeFragmentRoot, requireActivity(), 1)

        binding.mainRecycler.apply {
            recipesAdapter = RecipesListRecyclerAdapter(object : RecipesListRecyclerAdapter.OnItemClickListener{
                override fun click(recipes: Recipes) {
                //    (requireActivity() as MainActivity).launchDetailsFragment(recipes)
                }
            })
            //Присваиваем адаптер
            adapter = recipesAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())

        }
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

    override fun onStop() {
        super.onStop()
    }
}