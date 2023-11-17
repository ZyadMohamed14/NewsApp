package com.example.testnewsapp.myui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testnewsapp.repo.NewsRepo

class ViewModelProviderFactory(val application: Application,val newsRepo: NewsRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(application,newsRepo) as T
    }
}