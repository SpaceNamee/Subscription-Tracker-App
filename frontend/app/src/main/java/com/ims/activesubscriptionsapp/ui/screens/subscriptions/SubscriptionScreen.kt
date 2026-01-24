package com.ims.activesubscriptionsapp.ui.screens.subscriptions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.data.models.SubscriptionResponse
import com.ims.activesubscriptionsapp.ui.components.SubscriptionGridItem

@Composable
fun SubscriptionScreen(
    alreadyAdded: List<SubscriptionResponse>,
    onNext: (List<SubscriptionResponse>) -> Unit,
    onSkip: () -> Unit
) {
    val selectedItems = remember(alreadyAdded) {
        mutableStateListOf<SubscriptionResponse>().apply {
            addAll(alreadyAdded)
        }
    }


    // Lista de sugestões transformada para o modelo da API
    val all = remember {
        listOf(
            SubscriptionResponse(id = 0, name = "Custom", category = "Other", amount = 0.0, paymentPeriod = "monthly", nextPaymentDate = ""),
            SubscriptionResponse(id = 1, name = "YouTube", category = "Entertainment", amount = 0.0, paymentPeriod = "monthly", nextPaymentDate = ""),
            SubscriptionResponse(id = 2, name = "Spotify", category = "Music", amount = 0.0, paymentPeriod = "monthly", nextPaymentDate = ""),
            SubscriptionResponse(id = 3, name = "Netflix", category = "Entertainment", amount = 0.0, paymentPeriod = "monthly", nextPaymentDate = ""),
            SubscriptionResponse(id = 4, name = "X.com", category = "Social", amount = 0.0, paymentPeriod = "monthly", nextPaymentDate = ""),
            SubscriptionResponse(id = 5, name = "Roblox", category = "Games", amount = 0.0, paymentPeriod = "monthly", nextPaymentDate = ""),
            SubscriptionResponse(id = 6, name = "Paramount+", category = "Entertainment", amount = 0.0, paymentPeriod = "monthly", nextPaymentDate = "")
        )
    }

    // Filtra para não mostrar o que já foi adicionado (exceto o Custom)
    val available = all.filter { sub ->
        sub.name == "Custom" || !alreadyAdded.any { it.name == sub.name }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp)) {
        Text(
            text = "Skip",
            modifier = Modifier.align(Alignment.End).clickable { onSkip() },
            color = Color(0xFF007AFF),
            fontSize = 16.sp
        )

        Text(
            text = "Select your active subscriptions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )

        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF2F2F7),
                focusedIndicatorColor = Color.Transparent
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = available) { sub ->
                SubscriptionGridItem(
                    sub = sub,
                    isSelected = selectedItems.any { it.name == sub.name }
                ) {
                    if (selectedItems.any { it.name == sub.name }) {
                        selectedItems.removeAll { it.name == sub.name }
                    } else {
                        selectedItems.add(sub)
                    }
                }
            }
        }

        Button(
            onClick = { onNext(selectedItems.toList()) },
            enabled = selectedItems.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5387AC))
        ) {
            Text("Next", color = Color.White)
        }
    }
}