package com.ims.activesubscriptionsapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// CORREÇÃO: onBack e onNext alterados para tipos de função () -> Unit [conversation, 12]
@Composable
fun VerificationCodeScreen(onBack: () -> Unit, onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth()) { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
        Text("Check your Email", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.padding(top = 32.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(6) { Box(modifier = Modifier.size(45.dp).border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))) }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5387AC))) { Text("Verify") }
    }
}