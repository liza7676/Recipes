package com.example.recipes.rv_viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.data.entity.Recipes
import com.example.recipes.databinding.RecipesItemBinding
import com.bumptech.glide.Glide
class RecipesViewHolder (private val itemView: View
) : RecyclerView.ViewHolder(itemView) {
    private val recipesItemBinding = RecipesItemBinding.bind(itemView)

    //Привязываем View из layout к переменным
    private val title = recipesItemBinding.title
    private val poster = recipesItemBinding.poster

        fun bind(recipes: Recipes) {
        //Устанавливаем заголовок
        title.text = recipes.title
        //Устанавливаем постер
        Glide.with(itemView)
            .load(recipes.poster)
            .centerCrop()
            .into(poster)

    }
}