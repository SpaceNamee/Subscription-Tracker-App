package com.ims.activesubscriptionsapp.ui.screens.subscriptions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.ui.components.CategorySelector
import com.ims.activesubscriptionsapp.ui.components.EditableInputBlock
import com.ims.activesubscriptionsapp.ui.components.PeriodSelector
import com.ims.activesubscriptionsapp.ui.components.SubscriptionIconCircle
import com.ims.activesubscriptionsapp.data.models.Subscription


// --- ECRÃ 4: DETALHES ---
@Composable
fun EditSubscriptionDetailScreen(subscription: Subscription, onSave: (Subscription) -> Unit, onBack: () -> Unit) {
    var price by remember { mutableStateOf(subscription.price) }
    var period by remember { mutableStateOf(subscription.period) }
    var category by remember { mutableStateOf(subscription.category) }
    var date by remember { mutableStateOf(subscription.firstPaymentDate) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.background(Color.White, CircleShape)) { Icon(Icons.Default.ArrowBack, null) }
            Text("Subscription Detail", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))
        SubscriptionIconCircle(subscription, size = 80)
        Text(subscription.name, modifier = Modifier.padding(top = 16.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Active", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))
        EditableInputBlock("Payment", price) { price = it }
        PeriodSelector(period) { period = it }
        CategorySelector(category) { category = it }
        EditableInputBlock("Start Payment", date, hasArrow = true) { date = it }

        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = {}, modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color(0xFF006064))) {
                Text("Manage", color = Color(0xFF006064)); Icon(Icons.Default.CallMade, null, modifier = Modifier.size(18.dp))
            }
            Button(onClick = { onSave(subscription.copy(price = price, period = period, category = category, firstPaymentDate = date)) },
                modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006064))) {
                Text("Save ", color = Color.White); Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
            }
        }
    }
}