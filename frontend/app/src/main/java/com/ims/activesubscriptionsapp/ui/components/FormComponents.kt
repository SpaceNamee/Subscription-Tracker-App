package com.ims.activesubscriptionsapp.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun EditableInputBlock(label: String, value: String, hasArrow: Boolean = false, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF9E9E9E)).padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(label, color = Color(0xFFE0E0E0), fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(value = value, onValueChange = onValueChange, textStyle = TextStyle(color = Color.White, fontSize = 16.sp), cursorBrush = SolidColor(Color.White), modifier = Modifier.weight(1f))
            if (hasArrow) Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.LightGray)
        }
    }
}
@Composable
fun PeriodSelector(currentPeriod: String, onPeriodSelected: (String) -> Unit) {
    var exp by remember { mutableStateOf(false) }
    Box {
        Column(modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF9E9E9E)).clickable { exp = true }.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text("Period", color = Color(0xFFE0E0E0), fontSize = 12.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(currentPeriod, color = Color.White, modifier = Modifier.weight(1f), fontSize = 16.sp)
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.LightGray)
            }
        }
        DropdownMenu(expanded = exp, onDismissRequest = { exp = false }) {
            listOf("Week", "Month", "Year").forEach { p -> DropdownMenuItem(text = { Text(p) }, onClick = { onPeriodSelected(p); exp = false }) }
        }
    }
}
@Composable
fun CategorySelector(currentCategory: String, onCategorySelected: (String) -> Unit) {
    var exp by remember { mutableStateOf(false) }
    Box {
        Column(modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF9E9E9E)).clickable { exp = true }.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text("Category", color = Color(0xFFE0E0E0), fontSize = 12.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(currentCategory, color = Color.White, modifier = Modifier.weight(1f), fontSize = 16.sp)
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.LightGray)
            }
        }
        DropdownMenu(expanded = exp, onDismissRequest = { exp = false }) {
            listOf("Entertainment", "Education", "Fitness", "Productivity", "Lifestyle").forEach { cat ->
                DropdownMenuItem(text = { Text(cat) }, onClick = { onCategorySelected(cat); exp = false })
            }
        }
    }
}