package com.example.recipes.view.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipes.R
import com.example.recipes.data.entity.Recipes
import com.example.recipes.databinding.FragmentDetailsBinding
import com.example.recipes.viewmodel.DetailsFragmentViewModel
import com.example.recipes.viewmodel.ResultFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val scope = CoroutineScope(Dispatchers.IO)
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailsFragmentViewModel::class.java)
    }
    private lateinit var  recipes : Recipes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipes1 = arguments?.get("recipes") as Recipes
        recipes = Recipes(recipes1.id, recipes1.title, recipes1.poster)
        var rec: Recipes? = null
        MainScope().launch {
            val job = scope.async {
                rec = viewModel.interactor.searchFromFavorites(recipes1.id)
           }
           job.await()
            if (rec != null){
                recipes.isViewed = rec!!.isViewed
                recipes.isInFavorites = rec!!.isInFavorites
                if (recipes.isInFavorites == true)
                    binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
        }

    //Устанавливаем заголовок
        binding.detailsToolbar.title = recipes.title
        //Устанавливаем картинку
        Glide.with(this)
            .load(recipes.poster)
            .centerCrop()
            .into(binding.detailsPoster)
        //Устанавливаем описание
        binding.detailsDescription.text = "много много текста"//recipes.description

        binding.detailsFabFavorites.setImageResource(
            if (recipes.isInFavorites == true) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )
        binding.detailsFabFavorites.setOnClickListener {
            if (recipes.isInFavorites == false) {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_24)
                Executors.newSingleThreadExecutor().execute {
                    viewModel.interactor.delFromFavorites(recipes)
                    recipes.isInFavorites = true
                    viewModel.interactor.putToDb(listOf(recipes))
                }
            } else {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                Executors.newSingleThreadExecutor().execute {
                    viewModel.interactor.delFromFavorites(recipes)
                    recipes.isInFavorites = false
                    viewModel.interactor.putToDb(listOf(recipes))
                }
            }
        }

        binding.detailsFabShare.setOnClickListener {
            val s = recipes.id.toString()
            viewModel.getSummary(s)
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
                recipes.isViewed = true
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

    }

}