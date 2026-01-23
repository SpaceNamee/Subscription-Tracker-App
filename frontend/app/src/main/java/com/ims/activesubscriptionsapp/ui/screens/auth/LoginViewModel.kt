package com.ims.activesubscriptionsapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.models.LoginRequest
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<String>("")
    val loginState = _loginState.asStateFlow()

    fun performLogin(email: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = "Logging in..."

            try {
                // --- MODO DE SIMULAÇÃO (Enquanto o backend do grupo não funciona) ---
                delay(1500) // Simula espera de rede

                if (email.isNotEmpty() && pass.length >= 4) {
                    // Forçamos o estado para "Success"
                    _loginState.value = "Success"
                    println("DEBUG: Login simulado com sucesso para $email")
                } else {
                    _loginState.value = "Error: Credenciais inválidas (Simulação)"
                }

                /* // COMENTADO: Código real para quando o colega corrigir o backend
                val request = LoginRequest(email = email, password = pass)
                val response = RetrofitClient.api.login(request)
                if (response.isSuccessful) {
                    _loginState.value = "Success"
                } else {
                    _loginState.value = "Error: ${response.code()}"
                }
                */

            } catch (e: Exception) {
                _loginState.value = "Error: ${e.message}"
            }
        }
    }
}