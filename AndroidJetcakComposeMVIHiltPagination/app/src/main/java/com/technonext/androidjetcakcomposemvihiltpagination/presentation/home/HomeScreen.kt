package com.technonext.androidjetcakcomposemvihiltpagination.presentation.home

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asif047.androidjetpackcomposemvihiltpagination.R
import com.technonext.androidjetcakcomposemvihiltpagination.utils.LanguageManager


@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val languageManager = remember { LanguageManager(context) }

    var selectedLanguage by remember {
        mutableStateOf(languageManager.getLanguage())
    }

    // Update selected language on first composition
    LaunchedEffect(Unit) {
        selectedLanguage = languageManager.getLanguage()
        Log.d("HomeScreen", "Current language: $selectedLanguage")
    }

    val languages = listOf(
        LanguageManager.LANGUAGE_ENGLISH to stringResource(id = R.string.english),
        LanguageManager.LANGUAGE_BANGLA to stringResource(id = R.string.bangla)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_message),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.current_language),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Language Selection Radio Buttons
                Text(
                    text = stringResource(id = R.string.select_language),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    languages.forEach { (code, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .selectable(
                                    selected = selectedLanguage == code,
                                    onClick = {
                                        selectedLanguage = code
                                        languageManager.saveLanguage(code)
                                        // Restart activity to apply changes
                                        (context as? Activity)?.let {
                                            it.finish()
                                            it.startActivity(it.intent)
                                        }
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedLanguage == code,
                                onClick = {
                                    selectedLanguage = code
                                    languageManager.saveLanguage(code)
                                    (context as? Activity)?.let {
                                        it.finish()
                                        it.startActivity(it.intent)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = name)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Language Code: $selectedLanguage",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Android Version: ${android.os.Build.VERSION.SDK_INT}",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
