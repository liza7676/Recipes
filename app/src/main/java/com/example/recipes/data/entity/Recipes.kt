package com.example.recipes.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cached_recipes", indices = [Index(value = ["title"], unique = true)])
data class Recipes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_path") val poster: String?, //У нас будет приходить ссылка на картинку, так что теперь это String
    @ColumnInfo(name = "property") var property: Boolean?, //false - избранное, true - просмотренные
) : Parcelable
