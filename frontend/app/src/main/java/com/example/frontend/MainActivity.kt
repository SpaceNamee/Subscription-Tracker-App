package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontend.screens.*
import com.example.frontend.ui.theme.FrontendTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.data.PasswordResetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontendTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val passwordViewModel: PasswordResetViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "register"
    ) {

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.popBackStack()
                },
                onNavigateToNewPassword = {
                    navController.navigate("newpassword")
                }
            )
        }

        composable("newpassword") {
            NewPasswordScreen(
                viewModel = passwordViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCode = {
                    navController.navigate("code")
                }
            )
        }

        composable("code") {
            CodeScreen(
                viewModel = passwordViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToReset = {
                    navController.navigate("passwordcreate")
                }
            )
        }

        composable("passwordcreate") {
            PasswordCreate(
                viewModel = passwordViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.navigate("passwordchanged")
                }
            )
        }

        composable("passwordchanged") {
            PasswordChanged(
                onNavigateToLogin = {

                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}