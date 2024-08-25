package com.example.recipes

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.recipes.databinding.ActivityMainBinding
import com.example.recipes.view.fragments.DataSearch
import com.example.recipes.view.fragments.FavoritesFragment
import com.example.recipes.view.fragments.ResultFragment
import com.example.recipes.view.fragments.SearchFragment
import com.example.recipes.viewmodel.SearchFragmentViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val paramsSearch = DataSearch("Any", "Any", "Any", "Any", "Any")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: FavoritesFragment(), tag)
                    true
                }

                R.id.search -> {
                    val tag = "watch_later"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: SearchFragment(), tag)
                    true
                }
                else -> false
            }
        }
       //Thread.sleep(1000)
        supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_placeholder, SearchFragment())
        //.addToBackStack(null)
        .commit()

    }
    private fun checkFragmentExistence(tag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, tag)
            //.addToBackStack(null)
            .commit()
    }
    fun launchResultFragment() {

        val fragment = ResultFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            //.addToBackStack(null)
            .commit()
    }
}