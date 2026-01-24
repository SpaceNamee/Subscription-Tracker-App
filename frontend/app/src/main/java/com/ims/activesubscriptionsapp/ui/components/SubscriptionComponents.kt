package com.ims.activesubscriptionsapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.data.models.Subscription

@Composable
fun SubscriptionIconCircle(sub: Subscription, size: Int, isSmall: Boolean = false) {
    val displayLetter = when {
        sub.name == "Custom" && !isSmall -> "+"
        sub.name.isNotEmpty() -> sub.name.take(1).uppercase()
        else -> "?"
    }

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(if (isSmall) RoundedCornerShape(12.dp) else CircleShape)
            .background(if (sub.iconRes == null) sub.color else Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (sub.iconRes != null) {
            Image(
                painter = painterResource(id = sub.iconRes),
                contentDescription = sub.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (isSmall) 4.dp else 12.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = displayLetter,
                color = if (sub.name == "Custom" && !isSmall) Color.Black else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size / 2.5).sp
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SubscriptionRow(sub: Subscription, onClick: () -> Unit) {
    // Pegamos no tempo restante calculado pela lógica do modelo
    val timeRemaining = sub.getRemainingTime()

    // Cor de alerta: Vermelho se for hoje ou amanhã, cinza para o resto
    val statusColor = if (timeRemaining == "Today" || timeRemaining == "Tomorrow") {
        Color.Red
    } else {
        Color.Gray
    }

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

            // AGORA DINÂMICO: Usa o cálculo real do modelo
            Text(
                text = timeRemaining,
                color = statusColor,
                fontSize = 12.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text("${sub.price}€", fontWeight = FontWeight.Bold)
            Text("Manage >", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun SubscriptionGridItem(sub: Subscription, isSelected: Boolean, onClick: () -> Unit) {
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
                    null,
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