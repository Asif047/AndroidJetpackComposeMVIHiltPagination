package com.technonext.androidjetcakcomposemvihiltpagination

import android.app.Application
import com.technonext.androidjetcakcomposemvihiltpagination.utils.LanguageChangeHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize language helper
        val languageHelper = LanguageChangeHelper()
        val currentLanguage = languageHelper.getLanguageCode(this)

        // Apply current language on app start
        if (currentLanguage != "en") {
            languageHelper.changeLanguage(this, currentLanguage)
        }
    }
}

