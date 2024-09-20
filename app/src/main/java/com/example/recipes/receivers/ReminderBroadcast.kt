package com.example.recipes.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.recipes.data.entity.Recipes
import com.example.recipes.notifications.NotificationConstants
import com.example.recipes.notifications.NotificationHelper

class ReminderBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val bundle = intent?.getBundleExtra(NotificationConstants.RECIPE_BUNDLE_KEY)
        val recipes: Recipes = bundle?.get(NotificationConstants.RECIPE_KEY) as Recipes

        NotificationHelper.createNotification(context!!, recipes)
    }
}