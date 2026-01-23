package com.ims.activesubscriptionsapp.ui.screens.subscriptions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@Composable
fun EditSubscriptionDetailScreen(
    subscription: Subscription,
    onSave: (Subscription) -> Unit,
    onBack: () -> Unit
) {
    // IMPORTANTE: O remember(subscription) garante que quando mudas de subscrição na fila,
    // os estados internos (como o nome e preço) fazem reset para os valores do novo item.
    var name by remember(subscription) {
        mutableStateOf(if (subscription.name == "Custom") "" else subscription.name)
    }
    var price by remember(subscription) { mutableStateOf(subscription.price) }
    var period by remember(subscription) { mutableStateOf(subscription.period) }
    var category by remember(subscription) { mutableStateOf(subscription.category) }
    var date by remember(subscription) { mutableStateOf(subscription.firstPaymentDate) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Header ---
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.White, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, null)
            }
            Text(
                text = "Subscription Detail",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- Ícone Dinâmico ---
        // Se name estiver vazio, mostramos "Custom" (que ativa o "+" no seu componente)
        // Caso contrário, o componente mostrará a primeira letra do que o user digitar.
        SubscriptionIconCircle(
            sub = subscription.copy(name = if (name.isEmpty()) "Custom" else name),
            size = 80
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Campo de Nome (Apenas para Custom) ---
        if (subscription.name == "Custom") {
            EditableInputBlock(
                label = "Subscription Name",
                value = name,
                onValueChange = { name = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Active", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- Campos de Edição ---
        EditableInputBlock("Payment", price) { price = it }
        PeriodSelector(period) { period = it }
        CategorySelector(category) { category = it }
        EditableInputBlock("Start Payment", date, hasArrow = true) { date = it }

        Spacer(modifier = Modifier.weight(1f))

        // --- Botões de Ação ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { /* Lógica de gestão externa se necessária */ },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF006064))
            ) {
                Text("Manage", color = Color(0xFF006064))
                Icon(Icons.Default.CallMade, null, modifier = Modifier.size(18.dp))
            }

            Button(
                onClick = {
                    // Criamos a cópia final para salvar
                    val finalName = if (name.isEmpty()) "Custom" else name
                    onSave(subscription.copy(
                        name = finalName,
                        price = price,
                        period = period,
                        category = category,
                        firstPaymentDate = date
                    ))
                },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006064))
            ) {
                Text("Save ", color = Color.White)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
            }
        }
    }
}