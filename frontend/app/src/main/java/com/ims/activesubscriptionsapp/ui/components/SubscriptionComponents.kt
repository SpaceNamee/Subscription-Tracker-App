package com.ims.activesubscriptionsapp.ui.components

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.R
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import java.time.LocalDate
import java.time.temporal.ChronoUnit
object SubscriptionLogos {
    @DrawableRes
    val logos = mapOf(
        "YouTube" to R.drawable.ic_youtube,
        "Spotify" to R.drawable.ic_spotify,
        "Netflix" to R.drawable.ic_netflix,
        "X.com" to R.drawable.ic_xcom,
        "Roblox" to R.drawable.ic_roblox,
        "Paramount+" to R.drawable.ic_paramount
    )
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SubscriptionIconCircle(sub: SubscriptionResponse, size: Int, isSmall: Boolean = false) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(if (isSmall) RoundedCornerShape(12.dp) else CircleShape)
            .background(Color(0xFF5387AC)),
        contentAlignment = Alignment.Center
    ) {
        if (sub.name == "Custom") {
            //Logo is first letter of the name
            Text(
                text = sub.name.first().uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size / 2.5).sp
            )
        } else {
            val logo: Int? = SubscriptionLogos.logos[sub.name]
            if (logo != null) {
                Icon(
                    painter = painterResource(id = logo),
                    contentDescription = sub.name,
                    tint = Color.Unspecified,
                    modifier = Modifier.size((size * 1.0).dp)
                )
            } else {
                Text(
                    text = sub.name.first().uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (size / 2.5).sp
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SubscriptionRow(sub: SubscriptionResponse, onClick: () -> Unit) {
    val nextDate = LocalDate.parse(sub.nextPaymentDate.split("T")[0])
    val today = LocalDate.now()
    val daysBetween = ChronoUnit.DAYS.between(today, nextDate)
    val timeRemaining = when {
        daysBetween == 0L -> "Today"
        daysBetween == 1L -> "Tomorrow"
        daysBetween < 0L -> "Overdue"
        else -> "In $daysBetween days"
    }
    val statusColor = if (daysBetween <= 1L) Color.Red else Color.Gray
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubscriptionIconCircle(sub, 45, true)
        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
            Text(sub.name, fontWeight = FontWeight.Bold)
            Text(text = timeRemaining, color = statusColor, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("${"%.2f".format(sub.amount)}€", fontWeight = FontWeight.Bold)
            Text("Manage >", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SubscriptionGridItem(sub: SubscriptionResponse, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier.border(
                    if (isSelected) 4.dp else 0.dp,
                    Color(0xFF5387AC),
                    CircleShape
                )
            ) {
                SubscriptionIconCircle(sub, 80)
            }
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF5387AC),
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.White, CircleShape)
                )
            }
        }
        Text(sub.name, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
    }
}
