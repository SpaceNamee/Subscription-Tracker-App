package com.ims.activesubscriptionsapp.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ims.activesubscriptionsapp.ui.components.BottomNavBar
import com.ims.activesubscriptionsapp.ui.components.SubscriptionRow
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionViewModel
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    subscriptions: List<SubscriptionResponse>,
    onAddMore: () -> Unit,
    onEdit: (SubscriptionResponse) -> Unit,
    onNavigateToStats: () -> Unit,
    onSettingsClick: () -> Unit
) {

    // Data
    val today = LocalDate.now()
    val dayOfWeek = today
        .format(DateTimeFormatter.ofPattern("EEEE,", Locale.getDefault()))
        .replaceFirstChar { it.uppercase() }
    val fullDate = today.format(DateTimeFormatter.ofPattern("d MMMM", Locale.getDefault()))

    // Ordenar subscrições
    val sortedSubscriptions = remember(subscriptions) {
        subscriptions.sortedBy { it.nextPaymentDate }
    }

    // Total
    val total = subscriptions.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(dayOfWeek, color = Color.Gray, fontSize = 14.sp)
                Text(fullDate, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(28.dp))
            }
        }

        // Card Total
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("€", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Total for this month", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        "${"%.2f".format(total)}€",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }

        // Título + Add
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Active Subscriptions", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Button(
                onClick = onAddMore,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5387AC))
            ) {
                Text("Add")
            }
        }

        // Lista
        Box(modifier = Modifier.weight(1f)) {
            if (subscriptions.isEmpty()) {
                Text(
                    "No subscriptions yet",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(sortedSubscriptions) { sub ->
                        SubscriptionRow(sub) {
                            onEdit(sub)
                        }
                    }
                }
            }
        }

        BottomNavBar(
            onHomeClick = {},
            onStatsClick = onNavigateToStats
        )
    }
}