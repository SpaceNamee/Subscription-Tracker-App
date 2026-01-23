package com.ims.activesubscriptionsapp.ui.screens.auth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuccessScreen(msg: String, onDone: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(msg, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Button(onClick = onDone, modifier = Modifier.padding(top = 40.dp).fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5387AC))) { Text("Done") }
    }
}
