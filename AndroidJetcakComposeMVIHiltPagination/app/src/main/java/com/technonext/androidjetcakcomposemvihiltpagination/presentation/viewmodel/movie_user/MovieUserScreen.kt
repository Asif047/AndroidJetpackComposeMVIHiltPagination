package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.movie_user

// 📄 presentation/ui/MovieUserScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.*
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserIntent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiState

enum class ScreenTab {
    MOVIES, USERS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieUserScreen(
    viewModel: MovieAndUserViewModel = hiltViewModel()
) {
    val movieState by viewModel.movieUiState.collectAsStateWithLifecycle()
    val userState by viewModel.userUiState.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(ScreenTab.MOVIES) }

    // Handle events
    LaunchedEffect(Unit) {
        viewModel.movieUiEvent.collect { event ->
            when (event) {
                is MovieUiEvent.ShowError -> {
                    // Show snackbar or toast for movie errors
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.userUiEvent.collect { event ->
            when (event) {
                is UserUiEvent.ShowError -> {
                    // Show snackbar or toast for user errors
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Movies & Users App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Tab Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Movie Button
            Button(
                onClick = { selectedTab = ScreenTab.MOVIES },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == ScreenTab.MOVIES) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = if (selectedTab == ScreenTab.MOVIES) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            ) {
                Text("Movies")
            }

            // User Button
            Button(
                onClick = { selectedTab = ScreenTab.USERS },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == ScreenTab.USERS) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = if (selectedTab == ScreenTab.USERS) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            ) {
                Text("Users")
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            ScreenTab.MOVIES -> {
                MovieListContent(
                    movieState = movieState,
                    onLoadMore = { viewModel.loadMoreMovies() },
                    onRetry = { viewModel.handleMovieIntent(MovieIntent.LoadMovies) },
                    onRefresh = { viewModel.refreshMovies() },
                    onClearError = { viewModel.clearMovieError() }
                )
            }
            ScreenTab.USERS -> {
                UserListContent(
                    userState = userState,
                    onRetry = { viewModel.handleUserIntent(UserIntent.LoadUsers) },
                    onRefresh = { viewModel.refreshUsers() },
                    onClearError = { viewModel.clearUserError() }
                )
            }
        }
    }
}

@Composable
private fun MovieListContent(
    movieState: MovieUiState,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onClearError: () -> Unit
) {
    Column {
        // Header with refresh button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Movie List",
                style = MaterialTheme.typography.headlineSmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (movieState.error != null) {
                    TextButton(onClick = onClearError) {
                        Text("Clear Error")
                    }
                }
                Button(
                    onClick = onRefresh,
                    enabled = !movieState.isLoading
                ) {
                    Text("Refresh")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Content
        when {
            movieState.isLoading && movieState.movies.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            movieState.error != null && movieState.movies.isEmpty() -> {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading movies:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = movieState.error.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movieState.movies) { movie ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                if (movie.overview.isNotBlank()) {
                                    Text(
                                        text = movie.overview,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 2,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "Rating: ${movie.voteAverage}/10",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    if (movieState.hasMoreData) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = onLoadMore,
                                    enabled = !movieState.isLoadingMore
                                ) {
                                    if (movieState.isLoadingMore) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Text("Load More Movies")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserListContent(
    userState: UserUiState,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onClearError: () -> Unit
) {
    Column {
        // Header with refresh button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "User List",
                style = MaterialTheme.typography.headlineSmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (userState.error != null) {
                    TextButton(onClick = onClearError) {
                        Text("Clear Error")
                    }
                }
                Button(
                    onClick = onRefresh,
                    enabled = !userState.isLoading
                ) {
                    Text("Refresh")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Content
        when {
            userState.isLoading && userState.users.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            userState.error != null && userState.users.isEmpty() -> {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading users:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = userState.error.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(userState.users) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = user.email,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Username: ${user.username}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Phone: ${user.phone}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Company: ${user.company.name}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}