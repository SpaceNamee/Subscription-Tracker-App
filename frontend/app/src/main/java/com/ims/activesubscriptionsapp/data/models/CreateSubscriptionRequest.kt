package com.ims.activesubscriptionsapp.data.models
data class CreateSubscriptionRequest(
    val name: String,
    val amount: Double,
    val category: String,
    val payment_period: String,
    val first_payment_date: String,
    val currency: String
)
