package com.ims.activesubscriptionsapp.ui.screens.home
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.ui.components.BottomNavBar
import com.ims.activesubscriptionsapp.ui.components.SubscriptionRow
import com.ims.activesubscriptionsapp.data.models.Subscription
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// --- ECRÃ 3: HOME PAGE ---
@Composable
fun HomeScreen(subscriptions: List<Subscription>, onAddMore: () -> Unit, onEdit: (Subscription) -> Unit, onNavigateToStats: () -> Unit, onSettingsClick: () -> Unit) {
    val calendar = Calendar.getInstance()
    val dayOfWeek = SimpleDateFormat("EEEE,", Locale.getDefault()).format(calendar.time).replaceFirstChar { it.uppercase() }
    val fullDate = SimpleDateFormat("d MMMM", Locale.getDefault()).format(calendar.time)
    val total = subscriptions.sumOf { it.price.toDoubleOrNull() ?: 0.0 }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8)).padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(dayOfWeek, color = Color.Gray, fontSize = 14.sp)
                Text(fullDate, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            IconButton(onClick = onSettingsClick) { Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(28.dp)) }
        }

        Card(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFE0E0E0)), contentAlignment = Alignment.Center) { Text("$", fontWeight = FontWeight.Bold, fontSize = 20.sp) }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Total for this month", color = Color.Gray, fontSize = 14.sp)
                    Text("${"%.2f".format(total)}€", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(top = 30.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Active Subscription", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Button(onClick = onAddMore, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5387AC))) { Text("Add") }
        }

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(vertical = 16.dp)) {
            items(subscriptions) { sub -> SubscriptionRow(sub) { onEdit(sub) } }
        }
        BottomNavBar(onHomeClick = {}, onStatsClick = onNavigateToStats)
    }
}