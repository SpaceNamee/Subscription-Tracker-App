package com.ims.activesubscriptionsapp.data.remote
import com.ims.activesubscriptionsapp.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

interface AuthApi {
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

    @DELETE("api/v1/subscriptions/{subscription_id}")
    suspend fun deleteSubscription(@Path("subscription_id") id: Int): Response<Void>

    @POST("api/v1/subscriptions")
    suspend fun createSubscription(
        @Body request: CreateSubscriptionRequest
    ): Response<SubscriptionResponse>

    @PUT("api/v1/subscriptions/{subscription_id}")
    suspend fun updateSubscription(
        @Path("subscription_id") id: Int,
        @Body request: CreateSubscriptionRequest
    ): Response<SubscriptionResponse>

    @GET("api/v1/subscriptions")
    suspend fun getSubscriptions(): Response<SubscriptionListResponse>
}