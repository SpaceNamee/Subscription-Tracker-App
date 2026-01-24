package com.ims.activesubscriptionsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ims.activesubscriptionsapp.data.models.Subscription
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.EditSubscriptionDetailScreen
import com.ims.activesubscriptionsapp.ui.screens.settings.SettingsFlowManager
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionScreen
import com.ims.activesubscriptionsapp.ui.screens.home.HomeScreen
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticsScreen
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticsDetailScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.LoginScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation() {
    // 0. VIEWMODELS
    // Criamos o ViewModel aqui para podermos resetar o estado no Logout
    val loginViewModel: LoginViewModel = viewModel()

    // 0. ESTADO DE AUTENTICAÇÃO
    var isLoggedIn by remember { mutableStateOf(false) }

    // 1. Estados de Dados e Utilizador
    val finalizedSubscriptions = remember { mutableStateListOf<Subscription>() }
    val selectedQueue = remember { mutableStateListOf<Subscription>() }
    var userEmail by remember { mutableStateOf("user@gmail.com") }

    // 2. Estados de Navegação e Fluxo
    var currentIndex by remember { mutableStateOf(-1) }
    var showHome by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf("home") }

    // 3. Estados de Ecrãs Secundários
    var editingSub by remember { mutableStateOf<Subscription?>(null) }
    var selectedCategoryDetails by remember { mutableStateOf<String?>(null) }
    var settingsFlow by remember { mutableStateOf("none") }

    // --- LÓGICA DE DECISÃO PRINCIPAL ---
    if (!isLoggedIn) {
        LoginScreen(
            viewModel = loginViewModel, // Passamos o ViewModel instanciado acima
            onNavigateToRegister = { /* TODO */ },
            onNavigateToNewPassword = { /* TODO */ },
            onLoginSuccess = {
                isLoggedIn = true
            }
        )
    } else {
        when {
            settingsFlow != "none" -> {
                SettingsFlowManager(
                    initialEmail = userEmail,
                    flow = settingsFlow,
                    onNavigate = { settingsFlow = it },
                    onExit = { settingsFlow = "none" },
                    onLogout = {
                        // 1. Reset do Estado de Autenticação (UI)
                        isLoggedIn = false

                        // 2. RESET DO VIEWMODEL (Resolve o problema do login automático)
                        loginViewModel.resetState()

                        // 3. LIMPEZA DE DADOS
                        finalizedSubscriptions.clear()
                        selectedQueue.clear()
                        settingsFlow = "none"
                        showHome = false
                        currentIndex = -1
                        userEmail = "user@gmail.com"
                    }
                )
            }

            editingSub != null -> {
                EditSubscriptionDetailScreen(
                    subscription = editingSub!!,
                    onSave = { updated ->
                        val index = finalizedSubscriptions.indexOfFirst { it.name == updated.name }
                        if (index != -1) { finalizedSubscriptions[index] = updated }
                        editingSub = null
                    },
                    onBack = { editingSub = null }
                )
            }

            selectedCategoryDetails != null -> {
                StatisticsDetailScreen(
                    categoryName = selectedCategoryDetails!!,
                    subscriptions = finalizedSubscriptions.filter { it.category == selectedCategoryDetails },
                    onBack = { selectedCategoryDetails = null }
                )
            }

            showHome -> {
                if (currentTab == "home") {
                    HomeScreen(
                        subscriptions = finalizedSubscriptions,
                        onAddMore = {
                            showHome = false
                            currentIndex = -1
                        },
                        onEdit = { editingSub = it },
                        onNavigateToStats = { currentTab = "stats" },
                        onSettingsClick = { settingsFlow = "main" }
                    )
                } else {
                    StatisticsScreen(
                        subscriptions = finalizedSubscriptions,
                        onBack = { currentTab = "home" },
                        onNavigateToHome = { currentTab = "home" },
                        onCategoryClick = { selectedCategoryDetails = it }
                    )
                }
            }

            currentIndex == -1 -> {
                SubscriptionScreen(
                    alreadyAdded = finalizedSubscriptions,
                    onNext = { selections ->
                        if (selections.isNotEmpty()) {
                            selectedQueue.clear()
                            selectedQueue.addAll(selections)
                            currentIndex = 0
                        } else {
                            showHome = true
                        }
                    },
                    onSkip = {
                        selectedQueue.clear()
                        currentIndex = -1
                        showHome = true
                    }
                )
            }

            else -> {
                EditSubscriptionDetailScreen(
                    subscription = selectedQueue[currentIndex],
                    onSave = { updated ->
                        finalizedSubscriptions.add(updated)
                        if (currentIndex + 1 < selectedQueue.size) {
                            currentIndex++
                        } else {
                            selectedQueue.clear()
                            currentIndex = -1
                            showHome = true
                        }
                    },
                    onBack = {
                        currentIndex = -1
                        showHome = finalizedSubscriptions.isNotEmpty()
                    }
                )
            }
        }
    }
}