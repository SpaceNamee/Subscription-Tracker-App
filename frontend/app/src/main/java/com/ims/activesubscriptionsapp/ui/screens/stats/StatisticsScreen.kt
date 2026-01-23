package com.ims.activesubscriptionsapp.ui.screens.stats
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.data.models.Subscription
import com.ims.activesubscriptionsapp.ui.components.BottomNavBar
import com.ims.activesubscriptionsapp.ui.components.CategoryStatItem
import com.ims.activesubscriptionsapp.ui.components.SubscriptionRow

// --- ESTATÍSTICAS ---
@Composable
fun StatisticsScreen(subscriptions: List<Subscription>, onBack: () -> Unit, onNavigateToHome: () -> Unit, onCategoryClick: (String) -> Unit) {
    val total = subscriptions.sumOf { it.price.toDoubleOrNull() ?: 0.0 }
    val categories = subscriptions.groupBy { it.category }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8)).padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.background(Color.White, CircleShape)) { Icon(Icons.Default.ArrowBack, null) }
            Text("Statistics", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }
        Text("Total expenses for this month:", color = Color.Gray, modifier = Modifier.padding(top = 20.dp))
        Text("${"%.2f".format(total)}€", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Box(modifier = Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.size(140.dp).border(12.dp, Color(0xFF4CAF50), CircleShape))
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("18%", fontWeight = FontWeight.Bold, fontSize = 24.sp); Text("of total expenses", fontSize = 12.sp) }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            categories.forEach { (name, list) ->
                val catTotal = list.sumOf { it.price.toDoubleOrNull() ?: 0.0 }
                item { CategoryStatItem(name, "${list.size} transactions", "-${"%.2f".format(catTotal)}€") { onCategoryClick(name) } }
            }
        }
        BottomNavBar(onHomeClick = onNavigateToHome, onStatsClick = {})
    }
}

@Composable
fun StatisticDetailsScreen(categoryName: String, subscriptions: List<Subscription>, onBack: () -> Unit) {
    val groupedByDate = subscriptions.groupBy { it.firstPaymentDate }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8)).padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(40.dp))
        IconButton(onClick = onBack, modifier = Modifier.background(Color.White, CircleShape)) { Icon(Icons.Default.ArrowBack, null) }
        Text(categoryName, fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(top = 20.dp))

        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(vertical = 16.dp)) {
            groupedByDate.forEach { (date, list) ->
                item { Text(date, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp)) }
                items(list) { sub -> SubscriptionRow(sub) {} }
            }
        }
        BottomNavBar(onHomeClick = onBack, onStatsClick = {})
    }
}
