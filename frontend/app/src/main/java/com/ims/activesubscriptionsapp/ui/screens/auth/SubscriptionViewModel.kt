package com.ims.activesubscriptionsapp.ui.screens.subscriptions
import com.ims.activesubscriptionsapp.data.remote.SubscriptionRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import com.ims.activesubscriptionsapp.data.models.SubscriptionListResponse
// --------------------

class SubscriptionViewModel : ViewModel() {

    // Lista que o ecrã (Compose) vai observar
    private val _subscriptions = MutableStateFlow<List<SubscriptionResponse>>(emptyList())
    val subscriptions = _subscriptions.asStateFlow()

    // Estado de carregamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 1. FUNÇÃO PARA BUSCAR AS SUBS CRIADAS (O que faltava para aparecer após o login)
    fun fetchSubscriptions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.api.getSubscriptions()
                if (response.isSuccessful) {
                    // Atualiza a lista na UI com os dados reais do Python
                    _subscriptions.value = response.body()?.subscriptions ?: emptyList()
                    Log.d("API_TEST", "Dados carregados: ${_subscriptions.value.size} itens.")
                } else {
                    Log.e("API_TEST", "Erro ao buscar: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_TEST", "Falha de rede ao listar: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 2. FUNÇÃO PARA CRIAR (Atualizada para limpar a UI após sucesso)
    fun createSubscription() {
        val request = SubscriptionRequest(
            name = "Netflix",
            description = "Plano 4K",
            category = "streaming",
            amount = 15.99,
            currency = "EUR",
            paymentPeriod = "monthly",
            firstPaymentDate = "2026-01-24T12:00:00",
            websiteUrl = "https://netflix.com",
            logoUrl = null
        )

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.createSubscription(request)
                if (response.isSuccessful) {
                    Log.d("API_TEST", "Subscrição criada com sucesso!")
                    // MUITO IMPORTANTE: Após criar, mandamos buscar a lista atualizada
                    fetchSubscriptions()
                } else {
                    Log.e("API_TEST", "Erro ao criar: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API_TEST", "Falha catastrófica ao criar: ${e.message}")
            }
        }
    }
}