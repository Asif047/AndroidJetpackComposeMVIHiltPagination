package com.technonext.androidjetcakcomposemvihiltpagination

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.ui.auth.LoginScreen
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.ui.auth.LoginScreenRoute
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.ui.home.HomeScreen
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.ui.user.UserScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreenRoute(navController)
        }
        composable("home") {
            HomeScreen()
        }
        composable("user") {
            UserScreen()
        }
    }
}