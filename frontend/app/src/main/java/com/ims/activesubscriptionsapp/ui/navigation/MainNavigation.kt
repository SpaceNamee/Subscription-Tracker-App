package com.ims.activesubscriptionsapp.ui.navigation
import com.ims.activesubscriptionsapp.data.models.Subscription
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.EditSubscriptionDetailScreen
import com.ims.activesubscriptionsapp.ui.screens.settings.SettingsFlowManager
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionScreen
import com.ims.activesubscriptionsapp.ui.screens.home.HomeScreen
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticsScreen
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticDetailsScreen
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.collections.get

@Composable
fun MainNavigation() {
    val finalizedSubscriptions = remember { mutableStateListOf<Subscription>() }
    val selectedQueue = remember { mutableStateListOf<Subscription>() }
    var currentIndex by remember { mutableStateOf(-1) }
    var showHome by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf("home") }
    var editingSub by remember { mutableStateOf<Subscription?>(null) }
    var selectedCategoryDetails by remember { mutableStateOf<String?>(null) }
    var settingsFlow by remember { mutableStateOf("none") }

    if (settingsFlow != "none") {
        SettingsFlowManager(flow = settingsFlow, onNavigate = { settingsFlow = it }, onExit = { settingsFlow = "none" })
    } else if (editingSub != null) {
        EditSubscriptionDetailScreen(
            subscription = editingSub!!,
            onSave = { updated ->
                val index = finalizedSubscriptions.indexOfFirst { it.id == updated.id }
                if (index != -1) finalizedSubscriptions[index] = updated
                editingSub = null
            },
            onBack = { editingSub = null }
        )
    } else if (selectedCategoryDetails != null) {
        StatisticDetailsScreen(
            categoryName = selectedCategoryDetails!!,
            subscriptions = finalizedSubscriptions.filter { it.category == selectedCategoryDetails },
            onBack = { selectedCategoryDetails = null }
        )
    } else if (showHome) {
        if (currentTab == "home") {
            HomeScreen(
                subscriptions = finalizedSubscriptions,
                onAddMore = { showHome = false; currentIndex = -1 },
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
    } else if (currentIndex == -1) {
        SubscriptionScreen(
            alreadyAdded = finalizedSubscriptions,
            onNext = { selections ->
                selectedQueue.clear()
                selectedQueue.addAll(selections)
                currentIndex = 0
            },
            onSkip = { showHome = true }
        )
    } else {
        SubscriptionScreen(
            alreadyAdded = listOf(selectedQueue[currentIndex]),  // single item in a list
            onNext = { updatedList ->
                finalizedSubscriptions.addAll(updatedList)
                if (currentIndex + 1 < selectedQueue.size) {
                    currentIndex++
                } else {
                    showHome = true
                }
            },
            onSkip = { showHome = true }
        )
    }
}