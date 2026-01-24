package com.ims.activesubscriptionsapp.ui.screens.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.data.models.Subscription
import com.ims.activesubscriptionsapp.ui.components.SubscriptionIconCircle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsDetailScreen(
    categoryName: String,
    subscriptions: List<Subscription>,
    onBack: () -> Unit
) {
    // 1. Filtrar pela categoria selecionada
    // 2. Ordenar pela data do próximo pagamento
    val filteredAndSorted = remember(subscriptions, categoryName) {
        subscriptions
            .filter { it.category == categoryName }
            .sortedBy { it.getNextPaymentDate() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .statusBarsPadding() // <--- FIX: Resolve o botão "Back" muito para cima
            .padding(horizontal = 20.dp)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.White, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Statistics Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Lista Ordenada ---
        if (filteredAndSorted.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No subscriptions in $categoryName.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .navigationBarsPadding(), // Garante que a lista não fique sob a barra de gestos
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredAndSorted) { sub ->
                    SubscriptionDetailRow(sub)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SubscriptionDetailRow(sub: Subscription) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubscriptionIconCircle(sub = sub, size = 48, isSmall = true)

            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(text = sub.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                // Texto dinâmico com o tempo restante calculado
                val timeRemaining = sub.getRemainingTime()
                Text(
                    text = "${sub.period} • $timeRemaining",
                    fontSize = 12.sp,
                    color = if (timeRemaining == "Today" || timeRemaining == "Tomorrow") Color.Red else Color.Gray
                )
            }

            Text(
                text = "${sub.price}€",
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF006064),
                fontSize = 16.sp
            )
        }
    }
}