package com.technonext.androidjetcakcomposemvihiltpagination

import android.app.Application
import com.technonext.androidjetcakcomposemvihiltpagination.utils.LanguageManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val languageManager = LanguageManager(this)
        languageManager.setLocale(languageManager.getLanguage())
    }
}

