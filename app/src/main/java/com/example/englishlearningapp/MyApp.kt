package com.example.englishlearningapp

import android.app.Application
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.repository.WordRepository
import com.example.englishlearningapp.data.sampleWords
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val dao = AppDatabase.getDatabase(this).wordDao()
        val repo = WordRepository(dao)

        CoroutineScope(Dispatchers.IO).launch {
            repo.seedOrUpdate(sampleWords)
        }
    }
}
