package com.ims.activesubscriptionsapp.data.models
import com.google.gson.annotations.SerializedName

data class SubscriptionResponse(
    val id: Int = 0,
    val name: String,
    val category: String,
    val amount: Double,
    val currency: String = "EUR",
    @SerializedName("payment_period")
    val paymentPeriod: String,
    @SerializedName("next_payment_date")
    val nextPaymentDate: String,
    @SerializedName("logo_url")
    val logoUrl: String? = null
)