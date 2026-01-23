package com.ims.activesubscriptionsapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.models.RegisterRequest
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow<String>("")
    val registerState = _registerState.asStateFlow()

    fun performRegister(email: String, pass: String, repeatPass: String) {
        viewModelScope.launch {
            // 1. Validações Locais (Mantêm-se ativas)
            if (pass != repeatPass) {
                _registerState.value = "Error: Passwords do not match!"
                return@launch
            }

            if (pass.length < 8) {
                _registerState.value = "Error: Password must be at least 8 characters."
                return@launch
            }

            _registerState.value = "Creating account..."

            try {
                // 2. Simulação de Rede (Substitui o Retrofit temporariamente)
                delay(2000) // Faz o utilizador ver o "Creating account..."

                // FINGIMOS QUE O BACKEND RESPONDEU COM SUCESSO
                _registerState.value = "Success"

                // NOTA: Quando o teu colega corrigir o backend, voltas a usar isto:
                /*
                val request = RegisterRequest(email = email, password = pass)
                val response = RetrofitClient.api.register(request)
                if (response.success) {
                    _registerState.value = "Success"
                } else {
                    _registerState.value = "Error: Registration failed."
                }
                */

            } catch (e: Exception) {
                // Se houver algum erro catastrófico no código Android
                _registerState.value = "Error: ${e.message}"
            }
        }
    }
}