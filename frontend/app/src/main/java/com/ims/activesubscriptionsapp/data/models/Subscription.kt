package com.ims.activesubscriptionsapp.data.models
import androidx.compose.ui.graphics.Color
import java.util.UUID
data class Subscription(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val iconRes: Int? = null,
    val color: Color,
    var price: String = "0",
    var period: String = "Month",
    var category: String = "Entertainment",
    var firstPaymentDate: String = "20 Nov"
)