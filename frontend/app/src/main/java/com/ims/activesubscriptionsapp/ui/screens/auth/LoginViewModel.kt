package com.ims.activesubscriptionsapp.ui.screens.auth
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import com.ims.activesubscriptionsapp.data.models.LoginRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    // State to hold UI data
    private val _loginState = MutableStateFlow<String>("") // For status messages
    val loginState = _loginState.asStateFlow()

    fun performLogin(email: String, pass: String) {
        viewModelScope.launch {
            try {
                _loginState.value = "Logging in..."

                // Prepare the data matching the backend schema
                val request = LoginRequest(email = email, password = pass)

                // Call the API
                val response = RetrofitClient.api.login(request)

                // Success! You have the token: response.access_token
                _loginState.value = "Success! Token: ${response.access_token.take(10)}..."

                // TODO: Save this token in EncryptedSharedPreferences (DataStore)

            } catch (e: Exception) {
                // Handle errors (wrong password, server down, etc.)
                _loginState.value = "Error: ${e.message}"
            }
        }
    }
}