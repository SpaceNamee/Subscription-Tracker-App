package com.ims.activesubscriptionsapp.ui.screens.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import com.ims.activesubscriptionsapp.data.models.RegisterRequest

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow<String>("")
    val registerState = _registerState.asStateFlow()

    fun performRegister(email: String, pass: String, repeatPass: String) {
        viewModelScope.launch {
            if (pass != repeatPass) {
                _registerState.value = "Error: Passwords do not match!"
                return@launch
            }

            if (pass.length < 8) {
                _registerState.value = "Error: Password must be at least 8 characters."
                return@launch
            }

            try {
                _registerState.value = "Creating account..."

                val request = RegisterRequest(email = email, password = pass)
                val response = RetrofitClient.api.register(request)

                if (response.success) {
                    _registerState.value = "Success: ${response.message}"
                } else {
                    _registerState.value = "Registration failed."
                }
            } catch (e: Exception) {
                _registerState.value = "Error: ${e.message}"
            }
        }
    }
}