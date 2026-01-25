package com.ims.activesubscriptionsapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ims.activesubscriptionsapp.ui.navigation.MainNavigation
import com.ims.activesubscriptionsapp.ui.screens.auth.LoginScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.RegisterScreen
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionViewModel
import com.ims.activesubscriptionsapp.ui.theme.ActiveSubscriptionsAppTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActiveSubscriptionsAppTheme {
                var isLoggedIn by rememberSaveable { mutableStateOf(false) }
                var showRegister by rememberSaveable { mutableStateOf(false) }
                val subscriptionViewModel: SubscriptionViewModel = viewModel()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F8F8)
                ) {
                    if (!isLoggedIn) {
                        if (showRegister) {
                            RegisterScreen(
                                onRegisterSuccess = { showRegister = false },
                                onNavigateToLogin = { showRegister = false }
                            )
                        } else {
                            LoginScreen(
                                subscriptionViewModel = subscriptionViewModel,
                                onLoginSuccess = { isLoggedIn = true },
                                onNavigateToRegister = { showRegister = true },
                                onNavigateToNewPassword = { }
                            )
                        }
                    } else {
                        MainNavigation(
                            subscriptionViewModel = subscriptionViewModel,
                            onLogout = {
                                subscriptionViewModel.clearSubscriptions()
                                isLoggedIn = false
                            }
                        )
                    }
                }
            }
        }
    }
}