package com.example.testnewsapp.myui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkInfo
import android.os.Build
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testnewsapp.NewsApplication
import com.example.testnewsapp.models.Article
import com.example.testnewsapp.models.NewsResponse
import com.example.testnewsapp.repo.NewsRepo
import com.example.testnewsapp.utilies.Resources
import com.example.testnewsapp.utilies.Resources.Success
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(application: Application, val newsRepo: NewsRepo):AndroidViewModel(application) {
    init {
        BreakingNews("us")
    }
    val breakingNews:MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var breakingNewsRespons:NewsResponse?=null
    fun BreakingNews(countryCode:String)=viewModelScope.launch {

        safeBreakingNewsCalls(countryCode)
    }
    suspend fun safeBreakingNewsCalls(countryCode: String){
        breakingNews.postValue(Resources.Loading())
        try {
            if(hasInternetConnection()){
                val respone=newsRepo.getBreakingNews(countryCode,breakingNewsPage)
                breakingNews.postValue(handelBerakingNewsRespones(respone))

            }
            else {
                breakingNews.postValue(Resources.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException ->breakingNews.postValue(Resources.Error("Network Failer"))
                else -> breakingNews.postValue(Resources.Error("Conversiation error"))
            }

        }
    }

    val SearchNewsLiveData:MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var SearchNewsPage=1
    var searchNewsRespons:NewsResponse?=null
    fun searchNews(searchQuery:String)=viewModelScope.launch {safeSearchNewsCalls(searchQuery)}
    suspend fun safeSearchNewsCalls(searchQuery: String){
        SearchNewsLiveData.postValue(Resources.Loading())
        try {
            if(hasInternetConnection()){
                SearchNewsLiveData.postValue(Resources.Loading())
                val respone=newsRepo.getSearchNews(searchQuery,SearchNewsPage)
                SearchNewsLiveData.postValue(handelSearchNewsRespones(respone))
            }
            else {
                SearchNewsLiveData.postValue(Resources.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException ->SearchNewsLiveData.postValue(Resources.Error("Network Failer"))
                else -> SearchNewsLiveData.postValue(Resources.Error("Conversiation error"))
            }

        }
    }


    private fun handelBerakingNewsRespones(response: Response<NewsResponse>):Resources<NewsResponse>{
        if(response.isSuccessful){
           response.body()?.let {resultRespones ->
               breakingNewsPage++
               if(breakingNewsRespons==null){ breakingNewsRespons=resultRespones
               }else{
                   val oldArticles= breakingNewsRespons?.articles
                   val newArticles=resultRespones.articles
                   oldArticles?.addAll(newArticles)
               }
               return Resources.Success(breakingNewsRespons?:resultRespones)
           }
        }
        return Resources.Error(response.message())
    }
    private fun handelSearchNewsRespones(response: Response<NewsResponse>):Resources<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultRespones ->
                SearchNewsPage++
                if(searchNewsRespons==null){ searchNewsRespons=resultRespones
                }else{
                    val oldArticles= searchNewsRespons?.articles
                    val newArticles=resultRespones.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resources.Success(searchNewsRespons?:resultRespones)
            }
        }
        return Resources.Error(response.message())
    }
    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepo.upSert(article)
    }
    fun getSavedNews()= newsRepo.getSavedNews()
    fun delelwArticel(article: Article)= viewModelScope.launch {
        newsRepo.delete(article)
    }
    private fun hasInternetConnection():Boolean{
        val connectivityManager= getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val activeNetwork= connectivityManager.activeNetwork ?: return false

            val cabaplitie=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                cabaplitie.hasTransport(TRANSPORT_WIFI) -> true
                cabaplitie.hasTransport(TRANSPORT_ETHERNET) -> true
                cabaplitie.hasTransport(TRANSPORT_CELLULAR) -> true
                else ->false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_ETHERNET -> true
                    TYPE_MOBILE -> true
                    else -> false
                }
            }
        }
return false
    }
}
