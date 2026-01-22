package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontend.screens.*
import com.example.frontend.ui.theme.FrontendTheme

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