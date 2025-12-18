package com.example.englishlearningapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.englishlearningapp.data.dao.WordDao
import com.example.englishlearningapp.data.model.WordEntity

@Database(
    entities = [WordEntity::class],
    version = 3, // увеличиваем версию БД
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "english_learning_db"
                )
                    .fallbackToDestructiveMigration() // пересоздаёт БД при изменении схемы
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
