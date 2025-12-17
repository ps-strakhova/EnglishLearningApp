package com.example.englishlearningapp

import android.app.Application
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.sampleWords
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val db = AppDatabase.getDatabase(this)
        val dao = db.wordDao()

        CoroutineScope(Dispatchers.IO).launch {
            dao.insertWords(sampleWords)
        }
    }
}
