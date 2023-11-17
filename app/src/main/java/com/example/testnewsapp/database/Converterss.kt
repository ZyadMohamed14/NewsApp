package com.example.testnewsapp.database

import androidx.room.TypeConverter
import com.example.testnewsapp.models.Source

class Converterss {
    @TypeConverter
    fun fromSource(source: Source):String?{
        return source.name
    }
    @TypeConverter
    fun toSource(name: String): Source {
        // Check if the provided name is null and provide a default value if necessary
        val nonNullName = name ?: "DefaultName"

        return Source(nonNullName,nonNullName)

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
}
/*
  @Volatile
        private var instance:ArticalDataBase?=null
        private val Lock=Any()
        operator fun invoke(context:Context)= instance?: synchronized(Lock){
            instance?: createDatabase(context).also{ instance=it}
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(context.applicationContext,
                ArticalDataBase::class.java,
                "articale.db"
        ).build()
 */
 */