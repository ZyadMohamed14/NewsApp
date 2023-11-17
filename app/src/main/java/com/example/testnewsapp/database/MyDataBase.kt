package com.example.testnewsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testnewsapp.models.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converterss::class)
abstract class MyDataBase:RoomDatabase() {
    abstract fun articaleDao():MyDao
    companion object {
        @Volatile
        private var instance : MyDataBase?=null
        fun getInstance(context: Context): MyDataBase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MyDataBase::class.java,
                    "my_database"
                ).build().also { instance = it }
            }
        }
    }
}
/*
@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converterss::class)
abstract class ArticalDataBase :RoomDatabase() {
    abstract fun articaleDao():ArticalDao
    companion object {
        @Volatile
        private var instance : ArticalDataBase?=null
        fun getInstance(context: Context): ArticalDataBase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ArticalDataBase::class.java,
                    "my_database"
                ).build().also { instance = it }
            }
        }
    }

 */