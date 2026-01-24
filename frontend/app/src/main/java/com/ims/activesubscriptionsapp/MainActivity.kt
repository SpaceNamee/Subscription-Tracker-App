package com.ims.activesubscriptionsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ims.activesubscriptionsapp.ui.navigation.MainNavigation
import com.ims.activesubscriptionsapp.ui.screens.auth.LoginScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.RegisterScreen
import com.ims.activesubscriptionsapp.ui.theme.ActiveSubscriptionsAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ActiveSubscriptionsAppTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var showRegister by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F8F8)
                ) {
                    if (!isLoggedIn) {
                        // Se NÃO estiver logado, mostrar login/register
                        if (showRegister) {
                            RegisterScreen(
                                onRegisterSuccess = { showRegister = false },
                                onNavigateToLogin = { showRegister = false }
                            )
                        } else {
                            LoginScreen(
                                onLoginSuccess = { isLoggedIn = true },
                                onNavigateToRegister = { showRegister = true },
                                onNavigateToNewPassword = { }
                            )
                        }
                    } else {
                        // Se estiver logado, só aí iniciar MainNavigation
                        MainNavigation(
                            onLogout = { isLoggedIn = false }
                        )
                    }
                }
            }
        }
    }
}

