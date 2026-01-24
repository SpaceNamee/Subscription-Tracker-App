package com.ims.activesubscriptionsapp.ui.screens.subscriptions
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import com.ims.activesubscriptionsapp.ui.components.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun EditableInputBlock(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hasArrow: Boolean = false,
    enabled: Boolean = true // 1. Adiciona o parâmetro com valor padrão true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled, // 2. Passa o parâmetro para o TextField
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
// No trailingIcon do OutlinedTextField
            trailingIcon = if (hasArrow) {
                { Icon(Icons.Default.ArrowDropDown, null) } // ArrowDropDown costuma estar sempre disponível
            } else null
            // ... outros estilos que já tenhas (colors, textStyle, etc.)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditSubscriptionDetailScreen(
    subscription: SubscriptionResponse,
    onSave: (SubscriptionResponse) -> Unit,
    onBack: () -> Unit
) {
    // 1. Estados da Subscrição
    var name by remember(subscription) {
        mutableStateOf(if (subscription.name == "Custom") "" else subscription.name)
    }
    var amountStr by remember(subscription) { mutableStateOf(subscription.amount.toString()) }
    var period by remember(subscription) { mutableStateOf(subscription.paymentPeriod) }
    var category by remember(subscription) { mutableStateOf(subscription.category) }

    // VARIÁVEL ÚNICA: Próxima data de pagamento
    var nextDate by remember(subscription) { mutableStateOf(subscription.nextPaymentDate) }

    // 2. Estados do Calendário (DatePicker)
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Lógica do Diálogo do Calendário
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selected = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        nextDate = selected.format(DateTimeFormatter.ISO_LOCAL_DATE)
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
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Header ---
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.background(Color.White, CircleShape)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Subscription Detail",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        SubscriptionIconCircle(sub = subscription, size = 80)

        Spacer(modifier = Modifier.height(16.dp))

        if (subscription.name == "Custom") {
            EditableInputBlock(label = "Name", value = name, onValueChange = { name = it })
        } else {
            Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Campos de Edição ---
        EditableInputBlock("Payment Amount", amountStr) { amountStr = it }

        PeriodSelector(period) { period = it }

        CategorySelector(category) { category = it }

        // CAMPO DE DATA CLICÁVEL (Abre o Calendário)
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
        ) {
            EditableInputBlock(
                label = "Next Payment",
                value = nextDate,
                onValueChange = { },
                hasArrow = true,
                enabled = false // Desativa teclado para usar apenas o calendário
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Botões de Ação ---
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { /* Manage */ },
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
                        amount = amountStr.toDoubleOrNull() ?: 0.0,
                        paymentPeriod = period,
                        category = category,
                        nextPaymentDate = nextDate
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