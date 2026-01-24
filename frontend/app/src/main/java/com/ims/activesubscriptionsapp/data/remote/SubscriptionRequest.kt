package com.ims.activesubscriptionsapp.data.remote
import com.google.gson.annotations.SerializedName

data class SubscriptionRequest(
    val name: String,
    val description: String?,
    val category: String,
    val amount: Double,
    val currency: String,
    @SerializedName("payment_period")
    val paymentPeriod: String, // "monthly", "weekly", etc.
    @SerializedName("first_payment_date")
    val firstPaymentDate: String, // Formato "yyyy-MM-ddTHH:mm:ss"
    @SerializedName("website_url")
    val websiteUrl: String?,
    @SerializedName("logo_url")
    val logoUrl: String?
)
