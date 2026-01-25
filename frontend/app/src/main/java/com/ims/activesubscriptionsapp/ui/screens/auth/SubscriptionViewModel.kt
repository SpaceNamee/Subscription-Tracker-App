package com.ims.activesubscriptionsapp.ui.screens.subscriptions
import com.ims.activesubscriptionsapp.data.models.CreateSubscriptionRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import com.ims.activesubscriptionsapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.EditSubscriptionDetailScreen
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
                    _stateMessage.value = "Failed to load subscriptions"
                }
            } catch (e: Exception) {
                _stateMessage.value = e.message ?: "Unknown error"
            }
        }
    }
    fun addSubscriptionsFromSelection(selectedItems: List<SubscriptionResponse>) {
        viewModelScope.launch {
            selectedItems.forEach { item ->
                val amountToSend = item.amount
                val request = CreateSubscriptionRequest(
                    name = item.name,
                    amount = amountToSend,
                    //amount = if (item.amount == 0.0) 9.99 else item.amount,
                    category = item.category.lowercase(),
                    payment_period = item.paymentPeriod,
                    first_payment_date = if (item.nextPaymentDate.isNotBlank())
                        item.nextPaymentDate else "2026-01-25T09:00:00",
                    currency = item.currency.ifBlank { "EUR" }
                )
                try {
                    val response = RetrofitClient.api.createSubscription(request)
                    if (response.isSuccessful && response.body() != null) {
                        _subscriptions.value = _subscriptions.value + response.body()!!
                    } else {
                        _stateMessage.value = "422: ${response.errorBody()?.string()}"
                    }
                } catch (e: Exception) {
                    _stateMessage.value = e.message ?: "Error creating subscription"
                }
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
    /*
    fun deleteSubscription(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.deleteSubscription(id)
                if (response.isSuccessful) {
                    _subscriptions.value = _subscriptions.value.filter { it.id != id }
                }
            } catch (e: Exception) {
                _stateMessage.value = e.message ?: "Delete error"
            }
        }
    }*/
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
