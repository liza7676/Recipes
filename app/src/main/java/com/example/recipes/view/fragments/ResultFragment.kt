package com.example.recipes.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.MainActivity
import com.example.recipes.data.entity.Recipes
import com.example.recipes.databinding.FragmentResultBinding
import com.example.recipes.rv_adapter.RecipesListRecyclerAdapter
import com.example.recipes.utils.AutoDisposable
import com.example.recipes.utils.addTo
import com.example.recipes.viewmodel.ResultFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executors

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

        binding.mainRecycler.initSearchPagination()
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
                                    recipes.property = true
                                    val list = listOf(recipes)
                                    Executors.newSingleThreadExecutor().execute {
                                        viewModel.interactor.putToDb(list)
                                    }
                                    val intent = Intent()
                                    //Указываем action с которым он запускается
                                    intent.action = Intent.ACTION_SEND
                                    //Кладем данные о нашем рецепте
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Check out this recipes: ${recipes.title.toString()} \n\n ${url}"
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
                                recipes.property = false
                                val list = listOf(recipes)
                                Executors.newSingleThreadExecutor().execute {
                                    viewModel.interactor.putToDb(list)
                                }
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

    override fun onStop() {
        super.onStop()
    }
    private fun RecyclerView.initSearchPagination() {
        //Добавляем слушатель для скролла нашего RV
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //Если по оси Y есть изменение
                if (dy > 0) {
                    //Получаем количество видимых элементов
                    val visibleItemCount = recyclerView.layoutManager!!.childCount
                    //Получаем количесвто общих элементов
                    val totalItemCount = recyclerView.layoutManager!!.itemCount
                    //Находим первый видиимый элемент при скролле
                    val pastVisibleItemCount =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    //Совсем этим вызываем метод для пагинации
                    viewModel.doSearchPagination(
                        visibleItemCount,
                        totalItemCount,
                        pastVisibleItemCount
                    )
                    updateRecyclerView()
                }
            }
        }
        )
    }
    fun updateRecyclerView(){
        recipesAdapter.items.clear()
        viewModel.interactor.getFromList()?.let { recipesAdapter.addItems(it.toList()) }
    }
}