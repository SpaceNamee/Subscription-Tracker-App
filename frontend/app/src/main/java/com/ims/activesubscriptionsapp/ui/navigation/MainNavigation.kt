package com.ims.activesubscriptionsapp.ui.navigation
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ims.activesubscriptionsapp.data.models.CreateSubscriptionRequest
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import com.ims.activesubscriptionsapp.ui.screens.home.HomeScreen
import com.ims.activesubscriptionsapp.ui.screens.settings.SettingsFlowManager
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticsDetailScreen
import com.ims.activesubscriptionsapp.ui.screens.stats.StatisticsScreen
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.EditSubscriptionDetailScreen
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionScreen
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    onLogout: () -> Unit,
    subscriptionViewModel: SubscriptionViewModel = viewModel()
) {
    val finalizedSubscriptions by subscriptionViewModel.subscriptions.collectAsState()
    val selectedQueue = remember { mutableStateListOf<SubscriptionResponse>() }
    var currentIndex by remember { mutableStateOf(-1) }
    var showHome by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf("home") }
    var editingSub by remember { mutableStateOf<SubscriptionResponse?>(null) }
    var selectedCategoryDetails by remember { mutableStateOf<String?>(null) }
    var settingsFlow by remember { mutableStateOf("none") }
    val userEmail by remember { mutableStateOf("test@gmail.com") }

    when {
        //Configurations and exit
        settingsFlow != "none" -> {
            SettingsFlowManager(
                initialEmail = userEmail,
                flow = settingsFlow,
                onNavigate = { settingsFlow = it },
                onExit = { settingsFlow = "none" },
                onLogout = {
                    showHome = false
                    currentIndex = -1
                    settingsFlow = "none"
                    selectedQueue.clear()
                    subscriptionViewModel.clearSubscriptions()
                    onLogout()
                }
            )
        }
        //Edit new subscriptions
        !showHome && currentIndex >= 0 && currentIndex < selectedQueue.size -> {
            EditSubscriptionDetailScreen(
                subscription = selectedQueue[currentIndex],
                onSave = { updated ->
                    val apiPeriod = when (updated.paymentPeriod.trim()) {
                        "Week" -> "weekly"
                        "Month" -> "monthly"
                        "Year" -> "yearly"
                        else -> updated.paymentPeriod.lowercase()
                    }
                    val finalDate = if (updated.nextPaymentDate.isBlank())
                        "${LocalDate.now()}T12:00:00"
                    else if (updated.nextPaymentDate.contains("T"))
                        updated.nextPaymentDate
                    else
                        "${updated.nextPaymentDate}T12:00:00"
                    val request = CreateSubscriptionRequest(
                        name = updated.name,
                        amount = if (updated.amount == 0.0) 9.99 else updated.amount,
                        category = updated.category.lowercase(),
                        payment_period = apiPeriod,
                        first_payment_date = finalDate,
                        currency = updated.currency.ifBlank { "EUR" }
                    )
                    subscriptionViewModel.addSubscriptionsFromSelection(listOf(updated))
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
        //Edit existant subscriptions
        editingSub != null -> {
            EditSubscriptionDetailScreen(
                subscription = editingSub!!,
                onSave = { updated ->
                    val apiPeriod = when (updated.paymentPeriod.trim()) {
                        "Week" -> "weekly"
                        "Month" -> "monthly"
                        "Year" -> "yearly"
                        else -> updated.paymentPeriod.lowercase()
                    }
                    val finalDate = if (updated.nextPaymentDate.contains("T"))
                        updated.nextPaymentDate else "${updated.nextPaymentDate}T12:00:00"
                    val request = CreateSubscriptionRequest(
                        name = updated.name,
                        amount = updated.amount,
                        category = updated.category.lowercase(),
                        payment_period = apiPeriod,
                        first_payment_date = finalDate,
                        currency = updated.currency.ifBlank { "EUR" }
                    )
                    subscriptionViewModel.updateSubscription(updated.id, request)
                    editingSub = null
                },
                onBack = { editingSub = null }
            )
        }
        //Home
        selectedCategoryDetails != null -> {
            StatisticsDetailScreen(
                categoryName = selectedCategoryDetails!!,
                subscriptions = finalizedSubscriptions,
                onBack = { selectedCategoryDetails = null }
            )
        }
        showHome -> {
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
        }
        else -> {
            SubscriptionScreen(
                alreadyAdded = finalizedSubscriptions,
                onNext = { list ->
                    val newOnes = list.filter { newSub ->
                        finalizedSubscriptions.none { it.name == newSub.name }
                    }
                    if (newOnes.isNotEmpty()) {
                        selectedQueue.clear()
                        selectedQueue.addAll(newOnes)
                        currentIndex = 0
                        subscriptionViewModel.addSubscriptionsFromSelection(newOnes)
                    } else showHome = true
                },
                onSkip = { showHome = true }
            )
        }
    }
}
