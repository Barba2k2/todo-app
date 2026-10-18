package com.todoapp.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.todoapp.android.ui.screens.AuthScreen
import com.todoapp.android.ui.screens.ForgotPasswordScreen
import com.todoapp.android.ui.screens.HomeScreen
import com.todoapp.android.ui.screens.RegisterScreen
import com.todoapp.android.ui.screens.ResetPasswordScreen
import com.todoapp.android.ui.screens.TodoEditorScreen
import com.todoapp.android.ui.viewmodel.AuthViewModel

@Composable
fun TodoApp() {
  val navController = rememberNavController()
  val authViewModel: AuthViewModel = hiltViewModel()

  var startDestination by remember { mutableStateOf<String?>(null) }

  LaunchedEffect(Unit) {
    val hasSession = authViewModel.hasSession()
    startDestination = if (hasSession) "home" else "auth/login"
  }

  startDestination?.let { start ->
    AppNavHost(navController, authViewModel, start)
  }
}

@Composable
private fun AppNavHost(
  navController: NavHostController,
  authViewModel: AuthViewModel,
  startDestination: String
) {
  NavHost(navController = navController, startDestination = startDestination) {
    composable("auth/login") {
      AuthScreen(
        viewModel = authViewModel,
        onRegister = { navController.navigate("auth/register") },
        onForgot = { navController.navigate("auth/forgot") },
        onSuccess = { navController.navigate("home") { popUpTo("auth/login") { inclusive = true } } }
      )
    }
    composable("auth/register") {
      RegisterScreen(
        viewModel = authViewModel,
        onBack = { navController.popBackStack() },
        onSuccess = {
          navController.navigate("auth/login") {
            popUpTo("auth/register") { inclusive = true }
          }
        }
      )
    }
    composable("auth/forgot") {
      ForgotPasswordScreen(
        viewModel = authViewModel,
        onBack = { navController.popBackStack() },
        onReset = { navController.navigate("auth/reset") }
      )
    }
    composable("auth/reset") {
      ResetPasswordScreen(
        viewModel = authViewModel,
        onBack = { navController.popBackStack() }
      )
    }
        composable("home") {
            HomeScreen(
                onEditTodo = { id -> navController.navigate("todo/edit?id=$id") },
                onCreateTodo = { navController.navigate("todo/edit") },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("auth/login") { popUpTo("home") { inclusive = true } }
                }
            )
        }
        composable(
            "todo/edit?id={id}",
            arguments = listOf(navArgument("id") { nullable = true; defaultValue = "" })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.takeIf { it.isNotBlank() }
            TodoEditorScreen(
                todoId = id,
                onDone = { navController.popBackStack() }
            )
        }
    }
}
