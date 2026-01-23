package com.ims.activesubscriptionsapp.ui.screens.subscriptions
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.foundation.lazy.grid.items

// --- ECRÃ 1: SELEÇÃO ---
@Composable
fun SubscriptionScreen(alreadyAdded: List<Subscription>, onNext: (List<Subscription>) -> Unit, onSkip: () -> Unit) {
    val selectedItems = remember { mutableStateListOf<Subscription>() }
    val all = remember {
        listOf(
            Subscription(name = "Custom", color = Color.LightGray),
            Subscription(name = "YouTube", color = Color.Red),
            Subscription(name = "Paramount+", color = Color(0xFF0064FF)),
            Subscription(name = "Spotify", color = Color(0xFF1DB954)),
            Subscription(name = "X.com", color = Color.Black),
            Subscription(name = "FaceApp", color = Color(0xFFF5A623)),
            Subscription(name = "Kinopoisk", color = Color(0xFFFF6600)),
            Subscription(name = "Roblox", color = Color.DarkGray),
            Subscription(name = "Ivl", color = Color(0xFFE91E63))
        )
    }
    val available = all.filter { sub -> sub.name == "Custom" || !alreadyAdded.any { it.name == sub.name } }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp)) {
        Text("Skip", modifier = Modifier.align(Alignment.End).clickable { onSkip() }, color = Color(0xFF007AFF), fontSize = 16.sp)
        Text("Select your active subscriptions", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 20.dp))

        TextField(
            value = "", onValueChange = {},
            placeholder = { Text("Search", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(50.dp).clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFF2F2F7), focusedIndicatorColor = Color.Transparent)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            // Adicione explicitamente 'items = ' antes da sua variável 'available'
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
        Button(onClick = { onNext(selectedItems.toList()) }, enabled = selectedItems.isNotEmpty(), modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5387AC))) {
            Text("Next", color = Color.White)
        }
    }
}
