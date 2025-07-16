package com.technonext.androidjetcakcomposemvihiltpagination.presentation.ui.components.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asif047.androidjetpackcomposemvihiltpagination.R
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.language.LanguageViewModel

@Composable
fun LanguageSwitcher(
    viewModel: LanguageViewModel = viewModel()
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = stringResource(id = R.string.choose_language))
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { viewModel.onLanguageChange("en") }) {
                Text(text = stringResource(id = R.string.english))
            }
            Button(onClick = { viewModel.onLanguageChange("bn") }) {
                Text(text = stringResource(id = R.string.bangla))
            }
        }
    }
}