package com.technonext.androidjetcakcomposemvihiltpagination.presentation.ui.auth

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginRequest
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.auth.LoginViewModel
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.events.auth.LoginEvent
import com.technonext.androidjetcakcomposemvihiltpagination.util.DeveloperOptionsUtils

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (DeveloperOptionsUtils.isDeveloperOptionsEnabled(context)) {
            showDialog = true
        }
    }

    if (showDialog) {
        DeveloperOptionsDialog(
            onConfirm = {
                showDialog = false
                val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                context.startActivity(intent)
            },
            onDismiss = {},
            onExit = {
                (context as? Activity)?.finish()
            }
        )
    }

    LaunchedEffect(state.login) {
        if (state.login != null) {
            navController.navigate("home")
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.onAction(LoginEvent.Login(LoginRequest(username, password)))
            }) {
                Text("Login")
            }
            state.error?.let {
                Text(text = it)
            }
        }
    }
}

@Composable
fun DeveloperOptionsDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onExit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Developer Options Enabled") },
        text = { Text("Please disable developer options to continue.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Go to Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text("Exit")
            }
        }
    )
}
