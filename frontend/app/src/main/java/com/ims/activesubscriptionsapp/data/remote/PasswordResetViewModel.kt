package com.ims.activesubscriptionsapp.data.remote
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PasswordResetViewModel : ViewModel() {
    //State
    var email by mutableStateOf("")
    var code by mutableStateOf("")
    //UI States
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    //Navigation Flags (True when ready to move to next screen)
    var navigateToVerify by mutableStateOf(false)
    var navigateToReset by mutableStateOf(false)
    var navigateToLogin by mutableStateOf(false)
    //Actions
    fun onSendCodeClick() {
        if (email.isBlank()) {
            errorMessage = "Please enter your email."
            return
        }
        launchNetworkRequest {
            val response = RetrofitClient.api.sendResetCode(mapOf("email" to email))
            if (response.isSuccessful) {
                errorMessage = null // Clear errors
                navigateToVerify = true
            } else {
                errorMessage = "Email not found or server error."
            }
        }
    }
    fun onVerifyCodeClick() {
        if (code.length < 6) {
            errorMessage = "Please enter the full 6-digit code."
            return
        }
        launchNetworkRequest {
            val response = RetrofitClient.api.verifyResetCode(mapOf("email" to email, "code" to code))
            if (response.isSuccessful) {
                errorMessage = null
                navigateToReset = true
            } else {
                errorMessage = "Invalid code. Please try again."
            }
        }
    }
    fun onResetPasswordClick(newPassword: String) {
        if (newPassword.length < 6) {
            errorMessage = "Password must be at least 6 characters."
            return
        }
        launchNetworkRequest {
            val response = RetrofitClient.api.resetPassword(mapOf(
                "email" to email,
                "code" to code,
                "new_password" to newPassword
            ))
            if (response.isSuccessful) {
                errorMessage = null
                navigateToLogin = true
            } else {
                errorMessage = "Failed to reset password."
            }
        }
    }
    //Helper to reduce boilerplate
    private fun launchNetworkRequest(block: suspend () -> Unit) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}