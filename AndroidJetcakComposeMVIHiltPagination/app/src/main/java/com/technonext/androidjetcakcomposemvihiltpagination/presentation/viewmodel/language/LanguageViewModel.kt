package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class LanguageViewModel : ViewModel() {

    private val _currentLanguage = MutableStateFlow(Locale.getDefault().language)
    val currentLanguage: StateFlow<String> = _currentLanguage

    fun onLanguageChange(language: String) {
        viewModelScope.launch {
            _currentLanguage.value = language
        }
    }
}