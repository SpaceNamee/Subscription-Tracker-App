package com.ims.activesubscriptionsapp.data.models

import com.google.gson.annotations.SerializedName

// --- AUTENTICAÇÃO ---
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("token_type")
    val tokenType: String
)

data class RegisterRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val id: Int,
    val email: String,
    @SerializedName("is_active")
    val isActive: Boolean
)

// --- SUBSCRIÇÕES (ADICIONA ISTO ABAIXO) ---

// O que tu envias para o Python (POST)
data class SubscriptionRequest(
    val name: String,
    val amount: Double,
    val category: String,
    @SerializedName("payment_period") val paymentPeriod: String,
    @SerializedName("first_payment_date") val firstPaymentDate: String, // Formato: "2026-01-24T12:00:00"
    val currency: String = "EUR",
    val description: String? = null,
    @SerializedName("website_url") val websiteUrl: String? = null,
    @SerializedName("logo_url") val logoUrl: String? = null
)

// O que o Python devolve quando pedes a LISTA (GET)
data class SubscriptionListResponse(
    val subscriptions: List<SubscriptionResponse>,
    val total: Int
)