package com.ims.activesubscriptionsapp.ui.screens.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.models.RegisterRequest
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import com.ims.activesubscriptionsapp.data.remote.getErrorDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class RegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow<String>("")
    val registerState = _registerState.asStateFlow()
    fun performRegister(email: String, pass: String, repeatPass: String) {
        viewModelScope.launch {
            // 1. Local Validations
            if (email.isEmpty() || pass.isEmpty() || repeatPass.isEmpty()) {
                _registerState.value = "Error: All fields are required!"
                return@launch
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _registerState.value = "Error: Invalid email format!"
                return@launch
            }

            if (pass != repeatPass) {
                _registerState.value = "Error: Passwords do not match!"
                return@launch
            }

            if (pass.length < 8) {
                _registerState.value = "Error: Password must be at least 8 characters."
                return@launch
            }

            if (pass.length > 72) {
                _registerState.value = "Error: Password must be less than 72 characters."
                return@launch
            }

            _registerState.value = "Creating account..."

            try {
                // 2. Call real backend API
                val request = RegisterRequest(email = email, password = pass)
                val response = RetrofitClient.api.register(request)

                if (response.isSuccessful) {
                    _registerState.value = "Success"
                } else {
                    _registerState.value = "Error: ${response.getErrorDetail()}"
                }

            } catch (e: java.net.ConnectException) {
                _registerState.value = "Error: Cannot connect to server. Is backend running? Detail: $e"
            } catch (e: Exception) {
                _registerState.value = "Error: ${e.message}"
            }

        }
    }
}