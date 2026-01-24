package com.ims.activesubscriptionsapp.ui.screens.subscriptions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSubscriptionDetailScreen(
    subscription: Subscription,
    onSave: (Subscription) -> Unit,
    onBack: () -> Unit
) {
    // Verificação de segurança: Se o Android for inferior ao 8.0,
    // precisamos de garantir que as funções java.time não quebram a app.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        SubscriptionDetailContent(subscription, onSave, onBack)
    } else {
        // Fallback simples para versões muito antigas (API 23-25)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Esta funcionalidade requer Android 8.0 ou superior.")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubscriptionDetailContent(
    subscription: Subscription,
    onSave: (Subscription) -> Unit,
    onBack: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Estados locais
    var name by remember(subscription) {
        mutableStateOf(if (subscription.name == "Custom") "" else subscription.name)
    }
    var price by remember(subscription) { mutableStateOf(subscription.price) }
    var period by remember(subscription) { mutableStateOf(subscription.period) }
    var category by remember(subscription) { mutableStateOf(subscription.category) }

    // Estado para a Data (LocalDate)
    var selectedDate by remember(subscription) { mutableStateOf(subscription.firstPaymentDate) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Configuração do DatePicker
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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

        SubscriptionIconCircle(
            sub = subscription.copy(name = if (name.isEmpty()) "Custom" else name),
            size = 80
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        // Campo de Data que abre o DatePicker
        Box(modifier = Modifier.clickable { showDatePicker = true }) {
            EditableInputBlock(
                label = "Start Payment",
                value = selectedDate.format(formatter),
                hasArrow = true,
                onValueChange = { /* Não editável via teclado */ }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Botões de Ação ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { /* Lógica de gestão externa */ },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF006064))
            ) {
                Text("Manage", color = Color(0xFF006064))
                Icon(Icons.Default.CallMade, null, modifier = Modifier.size(18.dp))
            }

            Button(
                onClick = {
                    onSave(subscription.copy(
                        name = if (name.isEmpty()) "Custom" else name,
                        price = price,
                        period = period,
                        category = category,
                        firstPaymentDate = selectedDate
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