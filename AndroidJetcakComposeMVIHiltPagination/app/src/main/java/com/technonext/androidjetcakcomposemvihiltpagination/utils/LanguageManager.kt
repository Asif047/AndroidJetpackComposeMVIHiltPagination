package com.technonext.androidjetcakcomposemvihiltpagination.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

class LanguageManager(private val context: Context) {

    companion object {
        private const val PREF_NAME = "language_pref"
        private const val KEY_LANGUAGE = "selected_language"
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_BANGLA = "bn"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveLanguage(languageCode: String) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, languageCode).apply()
        setLocale(languageCode)
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
    }

    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    fun getCurrentLocale(): Locale {
        val languageCode = getLanguage()
        return Locale(languageCode)
    }
}