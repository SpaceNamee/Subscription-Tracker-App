package com.ims.activesubscriptionsapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.models.LoginRequest
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<String>("")
    val loginState = _loginState.asStateFlow()

    fun resetState() {
        _loginState.value = ""
    }

    fun performLogin(email: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = "Logging in..."
            try {
                val request = LoginRequest(email = email, password = pass)
                val response = RetrofitClient.api.login(request)

                if (response.isSuccessful && response.body() != null) {
                    // SUCESSO: Extraímos o token da resposta e guardamos no Client
                    val token = response.body()?.accessToken
                    RetrofitClient.authToken = token

                    _loginState.value = "Success"
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
}