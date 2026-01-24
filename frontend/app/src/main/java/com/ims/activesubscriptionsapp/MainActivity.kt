package com.ims.activesubscriptionsapp
import androidx.compose.runtime.saveable.rememberSaveable

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
                // Correção: importar rememberSaveable corretamente
                var isLoggedIn by rememberSaveable { mutableStateOf(false) }
                var showRegister by rememberSaveable { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F8F8)
                ) {
                    if (isLoggedIn == false) { // operador '!' não necessário
                        // Login / Register
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
                        // MainNavigation quando logado
                        MainNavigation(
                            onLogout = { isLoggedIn = false }
                        )
                    }
                }
            }
        }
    }
}
