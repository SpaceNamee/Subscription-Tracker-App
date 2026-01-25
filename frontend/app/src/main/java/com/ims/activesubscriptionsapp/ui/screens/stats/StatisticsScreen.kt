package com.ims.activesubscriptionsapp.ui.screens.stats
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.data.models.Subscription
import com.ims.activesubscriptionsapp.ui.components.BottomNavBar
val NeonColors = listOf(
    Color(0xFF00E5FF), // Cyan Neon
    Color(0xFF00FF87), // Verde Esmeralda
    Color(0xFFFF00E5), // Magenta/Pink
    Color(0xFFFFD600), // Amarelo Vibrante
    Color(0xFF7000FF), // Roxo Elétrico
    Color(0xFFFF5C00)  // Laranja Neon
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    subscriptions: List<SubscriptionResponse>,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val categoryTotals = remember(subscriptions) {
        subscriptions.groupBy { it.category }
            .mapValues { entry ->
                entry.value.sumOf { sub -> sub.amount ?: 0.0 }
            }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.White, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Statistics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(32.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (categoryTotals.isEmpty()) {
                    Text("No subscriptions found", color = Color.Gray)
                } else {
                    DonutChart(categoryTotals)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Categories", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            val categoriesList = categoryTotals.keys.toList()
            items(categoriesList.size) { index ->
                val categoryName = categoriesList[index]
                val amount = categoryTotals[categoryName] ?: 0.0
                CategoryRow(
                    name = categoryName,
                    amount = amount,
                    color = NeonColors[index % NeonColors.size]
                ) { onCategoryClick(categoryName) }
            }
        }
        BottomNavBar(onHomeClick = onNavigateToHome, onStatsClick = {})
    }
}

@Composable
fun DonutChart(categoryTotals: Map<String, Double>) {
    val total = categoryTotals.values.sum()
    Box(modifier = Modifier.size(220.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            categoryTotals.values.forEachIndexed { index, value ->
                val sweepAngle = if (total > 0) (value.toFloat() / total.toFloat()) * 360f else 0f
                drawArc(
                    color = NeonColors[index % NeonColors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 65f)
                )
                startAngle += sweepAngle
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Monthly", color = Color.Gray, fontSize = 14.sp)
            Text("${"%.2f".format(total)}€", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
        }
    }
}

@Composable
fun CategoryRow(name: String, amount: Double, color: Color, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, CircleShape)
            )
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 12.dp).weight(1f)
            )
            Text(
                text = "${"%.2f".format(amount)}€",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}