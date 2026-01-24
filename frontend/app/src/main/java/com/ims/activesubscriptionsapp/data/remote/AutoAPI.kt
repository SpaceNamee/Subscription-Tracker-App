package com.ims.activesubscriptionsapp.data.remote

import com.ims.activesubscriptionsapp.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    // --- Autenticação ---
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/v1/auth/forgot-password")
    suspend fun sendResetCode(@Body body: Map<String, String>): Response<Void>

    @POST("api/v1/auth/verify-code")
    suspend fun verifyResetCode(@Body body: Map<String, String>): Response<Void>

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body body: Map<String, String>): Response<Void>

    // --- Endpoints de Subscrições ---

    // 1. Criar Subscrição (POST)
    @POST("api/v1/subscriptions")
    suspend fun createSubscription(
        @Body request: SubscriptionRequest
    ): Response<SubscriptionResponse>

    // 2. Listar Subscrições (GET) -> ESTE ERA O QUE FALTAVA
    @GET("api/v1/subscriptions")
    suspend fun getSubscriptions(): Response<SubscriptionListResponse>
}