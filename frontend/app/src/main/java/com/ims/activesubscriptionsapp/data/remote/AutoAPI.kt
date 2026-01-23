package com.ims.activesubscriptionsapp.data.remote
import com.ims.activesubscriptionsapp.data.models.RegisterRequest
import com.ims.activesubscriptionsapp.data.models.LoginRequest
import com.ims.activesubscriptionsapp.data.models.LoginResponse
import com.ims.activesubscriptionsapp.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("/auth/forgot-password")
    suspend fun sendResetCode(@Body body: Map<String, String>): Response<Void>

    @POST("/auth/verify-code")
    suspend fun verifyResetCode(@Body body: Map<String, String>): Response<Void>

    @POST("/auth/reset-password")
    suspend fun resetPassword(@Body body: Map<String, String>): Response<Void>
}