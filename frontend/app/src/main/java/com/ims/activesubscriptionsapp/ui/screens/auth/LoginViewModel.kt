package com.ims.activesubscriptionsapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.models.LoginRequest
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<String>("")
    val loginState = _loginState.asStateFlow()

    fun resetState() {
        _loginState.value = ""
    }

    /**
     * Realiza o login e sincroniza imediatamente os dados do utilizador
     */
    fun performLogin(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        subscriptionViewModel: SubscriptionViewModel
    ) {
        viewModelScope.launch {
            _loginState.value = "Logging in..."
            try {
                val request = LoginRequest(email = email, password = pass)
                val response = RetrofitClient.api.login(request)

                if (response.isSuccessful && response.body() != null) {
                    // 1. Define o Token global para futuras chamadas à API
                    val token = response.body()?.accessToken
                    RetrofitClient.authToken = token

                    // 2. SINCRONIZAÇÃO: Carrega as subscrições reais do servidor
                    subscriptionViewModel.loadSubscriptions()

                    _loginState.value = "Success"

                    // 3. Notifica a UI (MainActivity) para mudar o ecrã
                    onSuccess()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Invalid Credentials"
                    _loginState.value = "Error: $errorMsg"
                }
            } catch (e: Exception) {
                _loginState.value = "Error: Failed to connect with the server."
                e.printStackTrace()
            }
        }
    }

    /**
     * Limpa a sessão e os dados em memória
     */
    fun logout(subscriptionViewModel: SubscriptionViewModel) {
        // Remove o token do cliente HTTP
        RetrofitClient.authToken = null

        // Limpa a lista de subscrições no ViewModel para o próximo utilizador
        subscriptionViewModel.clearSubscriptions()

        _loginState.value = "" // Reset do estado de login [4]
    }
}

/*package com.ims.activesubscriptionsapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.models.LoginRequest
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<String>("")
    val loginState = _loginState.asStateFlow()

    fun resetState() {
        _loginState.value = ""
    }

    fun performLogin(email: String, pass: String, onSuccess: () -> Unit, subscriptionViewModel: SubscriptionViewModel) {
        viewModelScope.launch {
            _loginState.value = "Logging in..."
            try {
                val request = LoginRequest(email = email, password = pass)
                val response = RetrofitClient.api.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()?.accessToken
                    RetrofitClient.authToken = token

                    // Carregar subscrições do usuário
                    subscriptionViewModel.loadSubscriptions()

                    _loginState.value = "Success"

                    // Avisa MainActivity que login foi sucesso
                    onSuccess()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Invalid Credentials"
                    _loginState.value = "Error: $errorMsg"
                }
            } catch (e: Exception) {
                _loginState.value = "Error: Failed to connect with the server."
                e.printStackTrace()
            }
        }
    }

    fun logout(subscriptionViewModel: SubscriptionViewModel) {
        RetrofitClient.authToken = null
        subscriptionViewModel.clearSubscriptions()
        _loginState.value = "" // Reset state
    }
}
*/