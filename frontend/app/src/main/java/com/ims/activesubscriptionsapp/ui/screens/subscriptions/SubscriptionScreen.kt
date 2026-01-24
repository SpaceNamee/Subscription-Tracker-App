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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.ui.components.SubscriptionGridItem
import com.ims.activesubscriptionsapp.data.models.Subscription
import java.time.LocalDate
import android.os.Build
import androidx.annotation.RequiresApi

@Composable
fun SubscriptionScreen(
    alreadyAdded: List<Subscription>,
    onNext: (List<Subscription>) -> Unit,
    onSkip: () -> Unit
) {
    val selectedItems = remember { mutableStateListOf<Subscription>() }

    val all = remember {
        listOf(
            Subscription(name = "Custom", color = Color.LightGray, firstPaymentDate = LocalDate.now()),
            Subscription(name = "YouTube", color = Color.Red, firstPaymentDate = LocalDate.now()),
            Subscription(name = "Paramount+", color = Color(0xFF0064FF), firstPaymentDate = LocalDate.now()),
            Subscription(name = "Spotify", color = Color(0xFF1DB954), firstPaymentDate = LocalDate.now()),
            Subscription(name = "X.com", color = Color.Black, firstPaymentDate = LocalDate.now()),
            Subscription(name = "FaceApp", color = Color(0xFFF5A623), firstPaymentDate = LocalDate.now()),
            Subscription(name = "Kinopoisk", color = Color(0xFFFF6600), firstPaymentDate = LocalDate.now()),
            Subscription(name = "Roblox", color = Color.DarkGray, firstPaymentDate = LocalDate.now()),
            Subscription(name = "Ivl", color = Color(0xFFE91E63), firstPaymentDate = LocalDate.now())
        )
    }

    val available = all.filter { sub ->
        sub.name == "Custom" || !alreadyAdded.any { it.name == sub.name }
    }

    // Adicionamos statusBarsPadding() na Column principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding() // Empurra o conteúdo para baixo da câmara/relógio
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Skip",
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onSkip() }
                .padding(bottom = 8.dp),
            color = Color(0xFF007AFF),
            fontSize = 16.sp
        )

        Text(
            text = "Select your active subscriptions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )

        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF2F2F7),
                focusedIndicatorColor = Color.Transparent
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            items(items = available) { sub ->
                SubscriptionGridItem(sub, selectedItems.contains(sub)) {
                    if (selectedItems.contains(sub)) {
                        selectedItems.remove(sub)
                    } else {
                        selectedItems.add(sub)
                    }
                }
            }
        }

        Button(
            onClick = { onNext(selectedItems.toList()) },
            enabled = selectedItems.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .navigationBarsPadding(), // Garante que o botão não fica colado ao fundo em gestos do Android
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5387AC))
        ) {
            Text("Next", color = Color.White)
        }
    }
}