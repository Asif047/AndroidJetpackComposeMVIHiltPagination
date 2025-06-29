package com.technonext.androidjetcakcomposemvihiltpagination.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(value: List<Int>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<Int> {
        return Gson().fromJson(value, object : TypeToken<List<Int>>() {}.type)
    }
}
