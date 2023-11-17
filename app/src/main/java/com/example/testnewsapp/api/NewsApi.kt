package com.example.testnewsapp.api

import com.example.testnewsapp.models.NewsResponse
import com.example.testnewsapp.utilies.Constans.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    companion object{

    }

    @GET("top-headlines")
  suspend  fun getBreakingNews(@Query("Country")countryCode:String="eg",
                               @Query("page")pageNumber:Int=1,
                               @Query("apiKey")apiKey:String=API_KEY):Response<NewsResponse>

    @GET("everything")
    suspend  fun searchForNews(@Query("q")searchQuery:String="eg",
                                 @Query("page")pageNumber:Int=1,
                               @Query("apiKey") apiKey:String=API_KEY):Response<NewsResponse>
}