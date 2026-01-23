package com.ims.activesubscriptionsapp.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavBar(onHomeClick: () -> Unit = {}, onStatsClick: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp).height(70.dp).clip(RoundedCornerShape(35.dp)).background(Color(0xFF4E4E4E)).padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = onHomeClick) { Icon(Icons.Default.Home, null, tint = Color.White) }
            IconButton(onClick = onStatsClick) { Icon(Icons.Default.PieChart, null, tint = Color.LightGray) }
        }
        Surface(modifier = Modifier.height(50.dp).width(180.dp), shape = RoundedCornerShape(25.dp), color = Color(0xFF7C4DFF).copy(alpha = 0.3f)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Icon(Icons.Default.Star, null, tint = Color.Magenta, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp)); Text("How can I help you?", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}