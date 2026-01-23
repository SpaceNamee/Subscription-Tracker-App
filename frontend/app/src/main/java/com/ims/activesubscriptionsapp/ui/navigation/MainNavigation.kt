package com.ims.activesubscriptionsapp.ui.navigation

import androidx.compose.runtime.*
import com.ims.activesubscriptionsapp.data.models.Subscription
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.EditSubscriptionDetailScreen
import com.ims.activesubscriptionsapp.ui.screens.settings.SettingsFlowManager
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionScreen
import com.ims.activesubscriptionsapp.ui.screens.home.HomeScreen
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticsScreen
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticDetailsScreen

@Composable
fun MainNavigation() {
    // 1. Estados de Dados
    val finalizedSubscriptions = remember { mutableStateListOf<Subscription>() }
    val selectedQueue = remember { mutableStateListOf<Subscription>() }

    // 2. Estados de Navegação e Fluxo
    // currentIndex: -1 = Seleção, 0+ = Fila de Edição
    var currentIndex by remember { mutableStateOf(-1) }
    var showHome by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf("home") }

    // 3. Estados de Ecrãs Secundários
    var editingSub by remember { mutableStateOf<Subscription?>(null) }
    var selectedCategoryDetails by remember { mutableStateOf<String?>(null) }
    var settingsFlow by remember { mutableStateOf("none") }

    when {
        // --- CASO 1: Definições (Settings) ---
        settingsFlow != "none" -> {
            SettingsFlowManager(
                flow = settingsFlow,
                onNavigate = { settingsFlow = it },
                onExit = { settingsFlow = "none" }
            )
        }

        // --- CASO 2: Edição Avulsa (Vindo da Home) ---
        editingSub != null -> {
            EditSubscriptionDetailScreen(
                subscription = editingSub!!,
                onSave = { updated ->
                    val index = finalizedSubscriptions.indexOfFirst { it.name == updated.name }
                    if (index != -1) {
                        finalizedSubscriptions[index] = updated
                    }
                    editingSub = null
                },
                onBack = { editingSub = null }
            )
        }

        // --- CASO 3: Detalhes de Estatísticas por Categoria ---
        selectedCategoryDetails != null -> {
            StatisticDetailsScreen(
                categoryName = selectedCategoryDetails!!,
                subscriptions = finalizedSubscriptions.filter { it.category == selectedCategoryDetails },
                onBack = { selectedCategoryDetails = null }
            )
        }

        // --- CASO 4: Dashboard Principal (Home ou Stats) ---
        showHome -> {
            if (currentTab == "home") {
                HomeScreen(
                    subscriptions = finalizedSubscriptions,
                    onAddMore = {
                        showHome = false
                        currentIndex = -1 // Volta para o ecrã de seleção inicial
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

        // --- CASO 5: Ecrã de SELEÇÃO ---
        currentIndex == -1 -> {
            SubscriptionScreen(
                alreadyAdded = finalizedSubscriptions,
                onNext = { selections ->
                    if (selections.isNotEmpty()) {
                        selectedQueue.clear()
                        selectedQueue.addAll(selections)
                        currentIndex = 0 // Inicia a fila de edição no primeiro item
                    } else {
                        showHome = true
                    }
                },
                onSkip = {
                    // Reset total para garantir que não há itens na fila ao saltar
                    selectedQueue.clear()
                    currentIndex = -1
                    showHome = true
                }
            )
        }

        // --- CASO 6: Fila de Edição Sequencial (Resolve o Loop e Duplicação) ---
        else -> {
            // Editamos o item atual da fila (selectedQueue[currentIndex])
            EditSubscriptionDetailScreen(
                subscription = selectedQueue[currentIndex],
                onSave = { updated ->
                    // Adicionamos a subscrição configurada à lista final (finalizedSubscriptions)
                    finalizedSubscriptions.add(updated)

                    if (currentIndex + 1 < selectedQueue.size) {
                        // Passa para a próxima subscrição (o EditDetailScreen fará reset automático)
                        currentIndex++
                    } else {
                        // Fim da fila: reseta a fila e mostra a Home
                        selectedQueue.clear()
                        currentIndex = -1
                        showHome = true
                    }
                },
                onBack = {
                    // Cancela o processo atual e volta à seleção ou Home
                    currentIndex = -1
                    showHome = finalizedSubscriptions.isNotEmpty()
                }
            )
        }
    }
}