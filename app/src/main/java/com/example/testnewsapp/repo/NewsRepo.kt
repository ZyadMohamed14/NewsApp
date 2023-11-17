package com.example.testnewsapp.repo

import com.example.testnewsapp.api.RetrofittInstance

import com.example.testnewsapp.database.MyDataBase
import com.example.testnewsapp.models.Article

class NewsRepo(val dataBase: MyDataBase) {

    suspend fun getBreakingNews(countryCode:String,pageNumber:Int)=RetrofittInstance.api.getBreakingNews(countryCode,pageNumber)
    suspend fun getSearchNews(searchQuery:String,pageNumber: Int)=RetrofittInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upSert(article: Article) = dataBase.articaleDao().upsert(article)
    suspend fun delete(article: Article) = dataBase.articaleDao().deletAtricale(article)
      fun getSavedNews() = dataBase.articaleDao().getAllArticels()

}
