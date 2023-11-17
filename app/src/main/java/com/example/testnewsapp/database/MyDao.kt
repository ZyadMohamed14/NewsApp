package com.example.testnewsapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testnewsapp.models.Article

@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(artical: Article):Long

    @Query("select * from articls")
    fun getAllArticels(): LiveData<List<Article>>

    @Delete
    suspend fun deletAtricale(artical: Article)
}
/*
@Dao
interface ArticalDao {

}
 */