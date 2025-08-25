package com.technonext.androidjetcakcomposemvihiltpagination.presentation.ui.user

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.events.user.UserEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.states.user.UserState
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserViewModel

@Composable
fun UserScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    // Handle error messages
    LaunchedEffect(state.error) {
        if (!state.error.isNullOrEmpty()) {
            context.showShortToast(state.error!!)
            // If you want to clear error after showing, you can add viewModel.clearError()
        }
    }

    UserScreen(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    state: UserState,
    onAction: (UserEvent) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.error != null -> {
                Text(
                    text = state.error ?: "Unknown error",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.users) { user ->
                        UserItem(user = user)
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = user.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = user.email)
        }
    }
}

// Extension function for showing toast
fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
