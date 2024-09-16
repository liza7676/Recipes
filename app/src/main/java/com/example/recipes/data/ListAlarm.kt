package com.example.recipes.data

import android.icu.util.Calendar

// обьект для хранения списка созданных нотификация
object ListAlarm {
    var listAlarm: MutableList<Alarm> = mutableListOf()

    // актуализация списка нотификаций, удаление устаревших
    fun updateList(){
        val curDate = Calendar.getInstance()
        val tempList = listAlarm.filterIndexed { index, alarm ->
            alarm.date.after(curDate)
        }
        listAlarm.clear()
        tempList.forEach{
            listAlarm.add(it)
        }
    }
    fun gatListAlarm():MutableList<Alarm>{
        return listAlarm
    }
}

data class Alarm(val name: String, val date: Calendar){

}