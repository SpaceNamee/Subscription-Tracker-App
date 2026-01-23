package com.ims.activesubscriptionsapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AuthStepScreen(title: String, label: String, onBack: () -> Unit, onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(20.dp)) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 20.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text(label) }, modifier = Modifier.fillMaxWidth().padding(top = 24.dp))
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5387AC))) { Text("Continue") }
    }
}