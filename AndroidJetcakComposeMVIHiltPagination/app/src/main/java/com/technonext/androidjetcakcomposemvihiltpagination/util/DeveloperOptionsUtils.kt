package com.technonext.androidjetcakcomposemvihiltpagination.util

import android.content.Context
import android.provider.Settings

object DeveloperOptionsUtils {
    fun isDeveloperOptionsEnabled(context: Context): Boolean {
        return Settings.Secure.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        ) == 1
    }
}