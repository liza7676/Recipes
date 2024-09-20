package com.example.recipes.view.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipes.R
import com.example.recipes.data.entity.Recipes
import com.example.recipes.databinding.FragmentDetailsBinding
import com.example.recipes.notifications.NotificationHelper
import com.example.recipes.viewmodel.DetailsFragmentViewModel
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

        viewModel.getSummary(recipes.id.toString())
        viewModel.interactor.setUrl( "Any")
        var i = 0
        while (i < 50) {
            val url = viewModel.interactor.getUrl()
            if (!url.equals("Any")) {
                val builder = SpannableStringBuilder()
                formatText(builder )
                binding.detailsDescription.setText(builder);
                break
            }
            i++
            Thread.sleep(100)
        }
        //Устанавливаем заголовок
        binding.detailsToolbar.title = recipes.title
        //Устанавливаем картинку
        Glide.with(this)
            .load(recipes.poster)
            .centerCrop()
            .into(binding.detailsPoster)

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
        binding.detailsFabCookLater.setOnClickListener {
            NotificationHelper.notificationSet(requireContext(), recipes)
        }

    }
    fun formatText(builder : SpannableStringBuilder){
        val summary = viewModel.interactor.getSummary()
        val servings = SpannableString("Порций:  " + summary.servings.toString() + "\n\n")
        val times = SpannableString("Время готовки:  " + summary.readyInMinutes.toString() + " минут\n\n")
        val veg = SpannableString("Вегетарианское:  " + (if(summary.vegetarian == true) "ДА" else "НЕТ") + "\n\n")
        val s = summary.dishTypes.toString()
        val type = SpannableString("Подходит для:  " + s.substring(1, s.length-1) + "\n\n")

        val flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

        servings.setSpan(TypefaceSpan("monospace"), 0, servings.length, flag)
        servings.setSpan(RelativeSizeSpan(1.2f), 0, servings.length, flag)
        times.setSpan(TypefaceSpan("monospace"), 0, times.length, flag)
        veg.setSpan(TypefaceSpan("monospace"), 0, veg.length, flag)
        veg.setSpan(BackgroundColorSpan(Color.YELLOW), veg.length-5, veg.length, flag)

        type.setSpan(ForegroundColorSpan(Color.BLUE), 0, type.length, flag)

        if (summary.servings != 0 ) builder.append(servings)
        if (summary.readyInMinutes != 0 ) builder.append(times)
        builder.append(veg)
        if(summary.dishTypes.size > 0) builder.append(type)

        if(summary.tmdbIngredients.size > 0){
            val ingredients = SpannableString("Ингридиенты:" + "\n")
            ingredients.setSpan(RelativeSizeSpan(2f), 0, ingredients.length, flag)
            builder.append(ingredients)
            summary.tmdbIngredients.forEach{
                if (it.length > 0) {
                    val ingr = SpannableString(" -  " + it + "\n")
                    ingr.setSpan(StyleSpan(Typeface.ITALIC), 0, ingr.length, flag)
                    builder.append(ingr)
                }
            }
        }
        if (summary.summary.length > 0){
            var ssumm = SpannableString("\nОписание:" + "\n")
            ssumm.setSpan(RelativeSizeSpan(2f), 0, ssumm.length, flag)
            builder.append(ssumm)
            ssumm = SpannableString(summary.summary + "\n\n")
            ssumm.setSpan(StyleSpan(Typeface.ITALIC), 0, ssumm.length, flag)
            builder.append(ssumm)
        }

        if(summary.tmdbInstructions.size > 0){
            val instructions = SpannableString("\nИнструкция:" + "\n")
            instructions.setSpan(RelativeSizeSpan(2f), 0, instructions.length, flag)
            builder.append(instructions)
            for (i in 0 .. summary.tmdbInstructions.size-1){
                if (summary.tmdbInstructions[i].length > 0) {
                    val ingr = SpannableString((i+1).toString() + ".  " + summary.tmdbInstructions[i] + "\n\n")
                    ingr.setSpan(StyleSpan(Typeface.ITALIC), 0, ingr.length, flag)
                    ingr.setSpan(RelativeSizeSpan(1.3f), 0, ingr.length, flag)
                    builder.append(ingr)
                }
            }
        }
    }
}