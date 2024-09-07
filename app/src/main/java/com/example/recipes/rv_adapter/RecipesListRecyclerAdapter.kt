package com.example.recipes.rv_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.data.entity.Recipes
import com.example.recipes.rv_viewholders.RecipesViewHolder

class RecipesListRecyclerAdapter (private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //Здесь у нас хранится список элементов для RV
    val items = mutableListOf<Recipes>()

    //Этот метод нужно переопределить на возврат количества элементов в списке RV
    override fun getItemCount() = items.size

    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего рецепта
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecipesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recipes_item, parent, false)
        )
    }

    //В этом методе будет привязка полей из объекта Recipes к View из recipes_item.xml
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecipesViewHolder -> {
                //Вызываем метод bind(), который мы создали, и передаем туда объект
                //из нашей базы данных с указанием позиции
                holder.bind(items[position])
                //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
                //например, картинку) и вызываем метод нашего листенера, который мы получаем из
                //конструктора адаптера
                val conteyner = holder.itemView.findViewById<CardView>(R.id.item_container)
                conteyner.setOnClickListener {
                    clickListener.click(items[position])
                }
            }
        }
    }

    //Метод для добавления объектов в наш список
    fun addItems(list: List<Recipes>) {
        //Сначала очищаем(если не реализовать DiffUtils)
        items.clear()
        //Добавляем
        items.addAll(list)
        //Уведомляем RV, что пришел новый список, и ему нужно заново все "привязывать"
        notifyDataSetChanged()
    }


    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(recipes: Recipes)
    }
}