package com.ims.activesubscriptionsapp.ui.screens.subscriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.models.CreateSubscriptionRequest
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import com.ims.activesubscriptionsapp.data.remote.getErrorDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel : ViewModel() {
    private val _subscriptions = MutableStateFlow<List<SubscriptionResponse>>(emptyList())
    val subscriptions = _subscriptions.asStateFlow()
    private val _stateMessage = MutableStateFlow("")
    val stateMessage = _stateMessage.asStateFlow()
    fun loadSubscriptions() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSubscriptions()
                if (response.isSuccessful && response.body() != null) {
                    _subscriptions.value = response.body()!!.subscriptions
                } else {
                    _stateMessage.value = response.getErrorDetail()
                }
            } catch (e: Exception) {
                _stateMessage.value = e.message ?: "Unknown error"
            }
        }
    }

    //ADD SUBSCRIPTION!
    fun addSubscription(subscription: SubscriptionResponse, request: CreateSubscriptionRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.createSubscription(request)
                if (response.isSuccessful && response.body() != null) {
                    val created = response.body()!!
                    _subscriptions.value = _subscriptions.value + created
                } else {
                    _stateMessage.value = "Error: ${response.errorBody()?.string()}"
                    _subscriptions.value = _subscriptions.value + subscription
                }
            } catch (e: Exception) {
                _stateMessage.value = e.message ?: "Error creating subscription"
                // Fallback local
                _subscriptions.value = _subscriptions.value + subscription
            }
        }
    }

    fun updateSubscription(id: Int, request: CreateSubscriptionRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.updateSubscription(id, request)
                if (response.isSuccessful && response.body() != null) {
                    _subscriptions.value = _subscriptions.value.map {
                        if (it.id == id) response.body()!! else it
                    }
                }
            } catch (e: Exception) {
                _stateMessage.value = e.message ?: "Update error"
            }
        }
    }

    fun deleteSubscription(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.deleteSubscription(id)
                if (response.isSuccessful) {
                    _subscriptions.value = _subscriptions.value.filter { it.id != id }
                    _stateMessage.value = "Subscription deleted"
                }
            } catch (e: Exception) {
                _stateMessage.value = e.message ?: "Delete error"
            }
        }
    }
    fun clearSubscriptions() {
        _subscriptions.value = emptyList()
    }
}