package com.ims.activesubscriptionsapp.data.models
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import java.util.UUID
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Subscription(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val iconRes: Int? = null,
    val color: Color,
    var price: String = "0",
    var period: String = "Month",
    var category: String = "Entertainment",
    var firstPaymentDate: LocalDate
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNextPaymentDate(): LocalDate {
        val today = LocalDate.now()
        var nextPayment = firstPaymentDate
        if (!nextPayment.isBefore(today)) {
            return nextPayment
        }
        while (nextPayment.isBefore(today)) {
            nextPayment = when {
                period.contains("Week", ignoreCase = true)  -> nextPayment.plusWeeks(1)
                period.contains("Month", ignoreCase = true) -> nextPayment.plusMonths(1)
                period.contains("Year", ignoreCase = true)  -> nextPayment.plusYears(1)
                else -> nextPayment.plusMonths(1)
            }
        }
        return nextPayment
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRemainingTime(): String {
        val today = LocalDate.now()
        val nextPayment = getNextPaymentDate()
        val daysBetween = ChronoUnit.DAYS.between(today, nextPayment)

        return when {
            daysBetween <= 0L -> "Today"
            daysBetween == 1L -> "Tomorrow"
            daysBetween < 7L  -> "In $daysBetween days"
            daysBetween < 30L -> {
                val weeks = daysBetween / 7
                if (weeks <= 1L) "In 1 week" else "In $weeks weeks"
            }
            else -> {
                val months = daysBetween / 30
                if (months <= 1L) "In 1 month" else "In $months months"
            }
        }
    }
}