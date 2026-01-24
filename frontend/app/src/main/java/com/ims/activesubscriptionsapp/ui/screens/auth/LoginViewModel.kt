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

    // NOVO: Função para limpar o estado ao fazer logout
    fun resetState() {
        _loginState.value = ""
    }

    fun performLogin(email: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = "Logging in..."
            try {
                delay(1500)
                if (email.isNotEmpty() && pass.length >= 4) {
                    _loginState.value = "Success"
                } else {
                    _loginState.value = "Error: Credenciais inválidas"
                }
            } catch (e: Exception) {
                _loginState.value = "Error: ${e.message}"
            }
        }
    }
}