package com.ims.activesubscriptionsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import com.ims.activesubscriptionsapp.ui.screens.home.HomeScreen
import com.ims.activesubscriptionsapp.ui.screens.settings.SettingsFlowManager
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticsDetailScreen
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticsScreen
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.EditSubscriptionDetailScreen
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    onLogout: () -> Unit
) {

    // ================== ESTADO GLOBAL ==================
    val finalizedSubscriptions = remember { mutableStateListOf<SubscriptionResponse>() } // todas as subscrições do utilizador
    val selectedQueue = remember { mutableStateListOf<SubscriptionResponse>() } // fila de novas subscrições a editar

    var currentIndex by remember { mutableStateOf(-1) } // índice da fila de edição
    var showHome by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf("home") } // "home" ou "stats"

    var editingSub by remember { mutableStateOf<SubscriptionResponse?>(null) } // edição a partir da home
    var selectedCategoryDetails by remember { mutableStateOf<String?>(null) } // detalhe de categoria

    var settingsFlow by remember { mutableStateOf("none") } // fluxo das settings
    var userEmail by remember { mutableStateOf("user@gmail.com") }

    // ================== LÓGICA DE NAVEGAÇÃO ==================
    when {

        // ================== SETTINGS ==================
        settingsFlow != "none" -> {
            SettingsFlowManager(
                initialEmail = userEmail,
                flow = settingsFlow,
                onNavigate = { settingsFlow = it },
                onExit = { settingsFlow = "none" },
                onLogout = {
                    // Limpar estado interno e voltar para login
                    finalizedSubscriptions.clear()
                    selectedQueue.clear()
                    currentIndex = -1
                    showHome = false
                    settingsFlow = "none"
                    onLogout()
                }
            )
        }

        // ================== EDIÇÃO DE NOVAS SUBSCRIÇÕES ==================
        !showHome && currentIndex >= 0 && currentIndex < selectedQueue.size -> {
            EditSubscriptionDetailScreen(
                subscription = selectedQueue[currentIndex],
                onSave = { updated ->

                    // Atualizar subscrição finalizada
                    val index = finalizedSubscriptions.indexOfFirst { it.id == updated.id }
                    if (index != -1) finalizedSubscriptions[index] = updated

                    // 🔹 Aqui podes chamar backend para salvar
                    // saveSubscriptionToBackend(updated)

                    if (currentIndex < selectedQueue.size - 1) {
                        currentIndex++
                    } else {
                        selectedQueue.clear()
                        currentIndex = -1
                        showHome = true
                    }
                },
                onBack = {
                    if (currentIndex > 0) currentIndex-- else {
                        selectedQueue.clear()
                        currentIndex = -1
                        showHome = true
                    }
                }
            )
        }

        // ================== EDIÇÃO DE SUBSCRIÇÕES EXISTENTES ==================
        editingSub != null -> {
            EditSubscriptionDetailScreen(
                subscription = editingSub!!,
                onSave = { updated ->
                    val index = finalizedSubscriptions.indexOfFirst { it.id == updated.id }
                    if (index != -1) finalizedSubscriptions[index] = updated

                    // 🔹 Salvar no backend
                    // saveSubscriptionToBackend(updated)

                    editingSub = null
                },
                onBack = { editingSub = null }
            )
        }

        // ================== DETALHE DE ESTATÍSTICAS ==================
        selectedCategoryDetails != null -> {
            StatisticsDetailScreen(
                categoryName = selectedCategoryDetails!!,
                subscriptions = finalizedSubscriptions,
                onBack = { selectedCategoryDetails = null }
            )
        }

        // ================== HOME / STATS ==================
        showHome -> {
            if (currentTab == "home") {
                HomeScreen(
                    subscriptions = finalizedSubscriptions,
                    onAddMore = {
                        // Abrir seleção de novas subscrições
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

        // ================== SELEÇÃO DE SUBSCRIÇÕES ==================
        else -> {
            SubscriptionScreen(
                alreadyAdded = finalizedSubscriptions,
                onNext = { list ->

                    // 1️⃣ Identificar apenas as novas subscrições
                    val newOnes = list.filter { newSub ->
                        finalizedSubscriptions.none { it.id == newSub.id }
                    }

                    // 2️⃣ Adicionar novas subscrições à lista final
                    newOnes.forEach { finalizedSubscriptions.add(it) }

                    // 🔹 Salvar novas subscrições no backend
                    // newOnes.forEach { saveSubscriptionToBackend(it) }

                    // 3️⃣ Editar apenas as novas subscrições
                    if (newOnes.isNotEmpty()) {
                        selectedQueue.clear()
                        selectedQueue.addAll(newOnes)
                        currentIndex = 0
                        showHome = false
                    } else {
                        showHome = true
                    }
                },
                onSkip = {
                    showHome = true
                }
            )
        }
    }
}
