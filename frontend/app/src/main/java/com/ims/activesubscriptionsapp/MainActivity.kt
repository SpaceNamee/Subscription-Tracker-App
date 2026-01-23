package com.ims.activesubscriptionsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ims.activesubscriptionsapp.ui.navigation.MainNavigation
import com.ims.activesubscriptionsapp.ui.theme.ActiveSubscriptionsAppTheme
import com.ims.activesubscriptionsapp.ui.screens.auth.LoginScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ActiveSubscriptionsAppTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var showRegister by remember { mutableStateOf(false) }

                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F8F8)) {
                    when {
                        isLoggedIn -> MainNavigation()
                        showRegister -> RegisterScreen(
                            onRegisterSuccess = { showRegister = false },
                            onNavigateToLogin = { showRegister = false }
                        )
                        else -> LoginScreen(
                            onLoginSuccess = { isLoggedIn = true },
                            onNavigateToRegister = { showRegister = true },
                            onNavigateToNewPassword = { /* Opcional */ }
                        )
                    }
                }
            }
        }
    }
}