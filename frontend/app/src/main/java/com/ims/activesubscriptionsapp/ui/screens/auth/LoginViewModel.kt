package com.ims.activesubscriptionsapp.ui.screens.auth
import com.ims.activesubscriptionsapp.data.remote.getErrorDetail

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
                    val token = response.body()?.accessToken
                    RetrofitClient.authToken = token
                    subscriptionViewModel.loadSubscriptions()
                    _loginState.value = "Success"
                    onSuccess()
                } else {
                    _loginState.value = "Error: ${response.getErrorDetail()}"
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
        _loginState.value = ""
    }
}