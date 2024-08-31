package com.example.recipes.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.MainActivity
import com.example.recipes.data.entity.Recipes
import com.example.recipes.databinding.FragmentResultBinding
import com.example.recipes.rv_adapter.RecipesListRecyclerAdapter
import com.example.recipes.utils.AutoDisposable
import com.example.recipes.utils.addTo
import com.example.recipes.viewmodel.ResultFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale
import java.util.concurrent.TimeUnit

import kotlinx.coroutines.*
class ResultFragment() : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(ResultFragmentViewModel::class.java)
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
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // viewModel.interactor.clearInCacheRecipes()
//        binding.searchView.setOnClickListener {
//            binding.searchView.isIconified = false
//        }
//        Observable.create(ObservableOnSubscribe<String> { subscriber ->
//            //Вешаем слушатель на клавиатуру
//            binding.searchView.setOnQueryTextListener(object :
//            //Вызывается на ввод символов
//                SearchView.OnQueryTextListener {
//                override fun onQueryTextChange(newText: String): Boolean {
//                    recipesAdapter.items.clear()
//                    subscriber.onNext(newText)
//                    return false
//                }
//
//                //Вызывается по нажатию кнопки "Поиск"
//                override fun onQueryTextSubmit(query: String): Boolean {
//                    subscriber.onNext(query)
//                    return false
//                }
//            })
//        })
//            .subscribeOn(Schedulers.io())
//            .map {
//                it.toLowerCase(Locale.getDefault()).trim()
//            }
//            .debounce(800, TimeUnit.MILLISECONDS)
//            .filter {
//                //Если в поиске пустое поле, возвращаем список  по умолчанию
//                viewModel.getRecipes((requireActivity() as MainActivity).paramsSearch)
//                it.isNotBlank()
//            }
//            .flatMap {
//                viewModel.getSearchResult(it)
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(
//                onError = {
//                    Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT)
//                        .show()
//                },
//                onNext = {
//                    recipesAdapter.addItems(it)
//                }
//            )
//            .addTo(autoDisposable)

        binding.mainRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            //оставим его пока пустым, он нам понадобится во второй части задания
            recipesAdapter =
                RecipesListRecyclerAdapter(object : RecipesListRecyclerAdapter.OnItemClickListener {
                    override fun click(recipes: Recipes) {


                        AlertDialog.Builder((requireActivity() as MainActivity))
                            .setTitle("${recipes.title.toString()}")
                            .setPositiveButton("Хотите перейти к рецепту?") { _, _ ->
                                viewModel.getSummary(recipes.id.toString())
                                var i = 0
                                var url = "Any"
                                while (i < 50) {
                                    url = viewModel.interactor.getUrl()
                                    if (!url.equals("Any")) {
                                        break
                                    }
                                    i++
                                    Thread.sleep(100)
                                }
                                if (!url.equals("Any")) {
                                    val intent = Intent()
                                    //Указываем action с которым он запускается
                                    intent.action = Intent.ACTION_SEND
                                    //Кладем данные о нашем фильме
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Check out this film: ${recipes.title.toString()} \n\n ${url}"
                                    )
                                    //Указываем MIME тип, чтобы система знала, какое приложения предложить
                                    intent.type = "text/plain"
                                    //Запускаем наше активити
                                    startActivity(Intent.createChooser(intent, "Share To:"))
                                } else {
                                    Toast.makeText(
                                        view.context,
                                        "Что то пошло не так.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .setNegativeButton("Добавить рецепт в избранное?") { _, _ ->
                                Toast.makeText(
                                    view.context,
                                    "Рецепт добавлен",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            .show()
                    }
                })
            //Присваиваем адаптер
            adapter = recipesAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())

        }
//        viewModel.recipesListData
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe { list ->
//                recipesAdapter.addItems(list)
//                recipesDataBase = list
//            }
//            .addTo(autoDisposable)

        viewModel.showProgressBar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.progressBar.isVisible = it
            }
            .addTo(autoDisposable)
        var i = 0
        while (i < 50) {
            val list = viewModel.interactor.getFromList()
            if (list != null) {
                recipesAdapter.addItems(list)
                break
            }
            i++
            Thread.sleep(100)
        }
    }
//    private fun initPullToRefresh() {
//        //Вешаем слушатель, чтобы вызвался pull to refresh
//        binding.pullToRefresh.setOnRefreshListener {
//            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод
//            recipesAdapter.items.clear()
//            //Делаем новый запрос фильмов на сервер
//            viewModel.getRecipes((requireActivity() as MainActivity).paramsSearch)
//            //Убираем крутящиеся колечко
//            binding.pullToRefresh.isRefreshing = false
//        }
//    }
    override fun onStop() {
        super.onStop()
    }
}