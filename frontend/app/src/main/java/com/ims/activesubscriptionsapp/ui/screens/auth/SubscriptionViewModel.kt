package com.ims.activesubscriptionsapp.ui.screens.subscriptions
import com.ims.activesubscriptionsapp.data.remote.SubscriptionRequest
import com.ims.activesubscriptionsapp.data.remote.AuthApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import com.ims.activesubscriptionsapp.data.models.SubscriptionListResponse
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<SubscriptionResponse>>(emptyList())
    val subscriptions = _subscriptions.asStateFlow()

    private val _stateMessage = MutableStateFlow<String>("")
    val stateMessage = _stateMessage.asStateFlow()

    fun loadSubscriptions() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSubscriptions()
                if (response.isSuccessful && response.body() != null) {
                    _subscriptions.value = response.body()!!.subscriptions
                    _stateMessage.value = "Subscriptions loaded"
                } else {
                    _stateMessage.value = "Failed to load subscriptions"
                }
            } catch (e: Exception) {
                _stateMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun addSubscription(request: SubscriptionRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.createSubscription(request)
                if (response.isSuccessful && response.body() != null) {
                    _subscriptions.value = _subscriptions.value + response.body()!!
                    _stateMessage.value = "Subscription added"
                } else {
                    _stateMessage.value = "Failed to add subscription"
                }
            } catch (e: Exception) {
                _stateMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateSubscription(id: Int, request: SubscriptionRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.updateSubscription(id, request)
                if (response.isSuccessful && response.body() != null) {
                    _subscriptions.value = _subscriptions.value.map {
                        if (it.id == id) response.body()!! else it
                    }
                    _stateMessage.value = "Subscription updated"
                } else {
                    _stateMessage.value = "Failed to update subscription"
                }
            } catch (e: Exception) {
                _stateMessage.value = "Error: ${e.message}"
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
                } else {
                    _stateMessage.value = "Failed to delete subscription"
                }
            } catch (e: Exception) {
                _stateMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun clearSubscriptions() {
        _subscriptions.value = emptyList()
    }
}
