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
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun EditableInputBlock(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hasArrow: Boolean = false,
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = if (hasArrow) {
                { Icon(Icons.Default.ArrowDropDown, null) }
            } else null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditSubscriptionDetailScreen(
    subscription: SubscriptionResponse,
    onSave: (SubscriptionResponse) -> Unit,
    onBack: () -> Unit,
    onDelete: (Int) -> Unit
)
{
    var name by remember(subscription) { mutableStateOf(if (subscription.name == "Custom") "" else subscription.name) }
    var amountStr by remember(subscription) { mutableStateOf(if (subscription.amount == 0.0) "" else subscription.amount.toString()) }
    var period by remember(subscription) { mutableStateOf(subscription.paymentPeriod) }
    var category by remember(subscription) { mutableStateOf(subscription.category) }
    var nextDate by remember(subscription) {
        mutableStateOf(if (subscription.nextPaymentDate.isBlank()) LocalDate.now().toString() else subscription.nextPaymentDate)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

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
        // Top Bar
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

        // Icon
        SubscriptionIconCircle(sub = subscription, size = 80)

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        if (subscription.name == "Custom") {
            EditableInputBlock(label = "Name", value = name, onValueChange = { name = it })
        } else {
            Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Amount, Period, Category
        EditableInputBlock("Payment Amount", amountStr) { amountStr = it }
        PeriodSelector(period) { period = it }
        CategorySelector(category) { category = it }

        // Next Payment Date
        Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
            EditableInputBlock(
                label = "Next Payment",
                value = nextDate,
                onValueChange = { },
                hasArrow = true,
                enabled = false
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Buttons: Delete e Save
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Delete Button
            OutlinedButton(
                onClick = { onDelete(subscription.id) },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White
                )
            ) {
                Text("Delete")
            }

            // Save Button
            Button(
                onClick = {
                    val sanitizedAmount = amountStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                    onSave(subscription.copy(
                        name = if (name.isEmpty()) "Custom" else name,
                        amount = sanitizedAmount,
                        paymentPeriod = period,
                        category = category,
                        nextPaymentDate = nextDate
                    ))
                },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006064))
            ) {
                Text("Save", color = Color.White)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
            }
        }
    }
}

/*
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
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun EditableInputBlock(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hasArrow: Boolean = false,
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = if (hasArrow) {
                { Icon(Icons.Default.ArrowDropDown, null) } // ArrowDropDown costuma estar sempre disponível
            } else null
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditSubscriptionDetailScreen(
    subscription: SubscriptionResponse,
    onSave: (SubscriptionResponse) -> Unit,
    onBack: () -> Unit,
    onDelete: (Int) -> Unit = { id ->
        viewModel.deleteSubscription(id)
        navController.popBackStack()
    }
) {
    var name by remember(subscription) {
        mutableStateOf(if (subscription.name == "Custom") "" else subscription.name)
    }
    //var amountStr by remember(subscription) { mutableStateOf(subscription.amount.toString()) }
    var amountStr by remember(subscription) {
        mutableStateOf(
            if (subscription.amount == 0.0) "" else subscription.amount.toString()
        )
    }
    var period by remember(subscription) { mutableStateOf(subscription.paymentPeriod) }
    var category by remember(subscription) { mutableStateOf(subscription.category) }
    //var nextDate by remember(subscription) { mutableStateOf(subscription.nextPaymentDate) }
    var nextDate by remember(subscription) {
        mutableStateOf(
            if (subscription.nextPaymentDate.isBlank()) LocalDate.now().toString()
            else subscription.nextPaymentDate
        )
    }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
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
        EditableInputBlock("Payment Amount", amountStr) { amountStr = it }
        PeriodSelector(period) { period = it }
        CategorySelector(category) { category = it }
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
        ) {
            EditableInputBlock(
                label = "Next Payment",
                value = nextDate,
                onValueChange = { },
                hasArrow = true,
                enabled = false
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { onDelete(subscription.id) },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF006064))
            ) {
                Text("Delete", color = Color(0xFF006064))
                //Icon(Icons.Default.CallMade, null, modifier = Modifier.size(18.dp))
            }
            Button(
                onClick = {
                    val sanitizedAmount = amountStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                    //val sanitizedAmount = amountStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                    onSave(subscription.copy(
                        name = if (name.isEmpty()) "Custom" else name,
                        amount = sanitizedAmount,
                        paymentPeriod = period,
                        category = category,
                        nextPaymentDate = nextDate
                    ))
                },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006064))
            ) {
                Text("Save ", color = Color(color=0xFFD32F2F))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
            }
        }
    }
}*/