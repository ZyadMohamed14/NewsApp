package com.example.testnewsapp.myui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.testnewsapp.R
import com.example.testnewsapp.database.MyDataBase
import com.example.testnewsapp.databinding.ActivityNewsBinding
import com.example.testnewsapp.myui.viewmodel.NewsViewModel
import com.example.testnewsapp.myui.viewmodel.ViewModelProviderFactory
import com.example.testnewsapp.repo.NewsRepo

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
     lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel and NewsRepo
        val db= MyDataBase.getInstance(this)
        val newsRepo = NewsRepo(db)
        val viewModelFactory = ViewModelProviderFactory(application,newsRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        // Get the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

    }

}