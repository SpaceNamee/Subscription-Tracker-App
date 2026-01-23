package com.ims.activesubscriptionsapp.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.ui.screens.auth.AuthStepScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.AuthStepScreen
import com.ims.activesubscriptionsapp.ui.components.SettingsActionItem
import com.ims.activesubscriptionsapp.ui.screens.auth.SetPasswordScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.SuccessScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.VerificationCodeScreen
import com.ims.activesubscriptionsapp.ui.components.CurrencyDisplayItem

// --- DEFINIÇÕES ---
@Composable
fun SettingsFlowManager(flow: String, onNavigate: (String) -> Unit, onExit: () -> Unit) {
    when (flow) {
        "main" -> SettingsScreen(onBack = onExit, onEmail = { onNavigate("email1") }, onPass = { onNavigate("pass1") })
        "email1" -> AuthStepScreen("Type your new email", "Email", onBack = { onNavigate("main") }, onNext = { onNavigate("email2") })
        "email2" -> VerificationCodeScreen(onBack = { onNavigate("email1") }, onNext = { onNavigate("emailSuccess") })
        "emailSuccess" -> SuccessScreen("Email Changed Successfully", onDone = { onNavigate("main") })
        "pass1" -> AuthStepScreen("Email Verification", "Email", onBack = { onNavigate("main") }, onNext = { onNavigate("pass2") })
        "pass2" -> VerificationCodeScreen(onBack = { onNavigate("pass1") }, onNext = { onNavigate("pass3") })
        "pass3" -> SetPasswordScreen(onBack = { onNavigate("pass2") }, onNext = { onNavigate("passSuccess") })
        "passSuccess" -> SuccessScreen("Password Changed Successfully", onDone = { onNavigate("main") })
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit, onEmail: () -> Unit, onPass: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8)).padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.background(Color.White, CircleShape)) { Icon(Icons.Default.ArrowBack, null) }
            Text("Settings", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }
        Text("Account settings", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 24.dp))
        SettingsActionItem("Email", "m****@gmail.com", Icons.Default.Email, onEmail)
        SettingsActionItem("Password", "********", Icons.Default.Lock, onPass)
        Text("Other info", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 16.dp))
        CurrencyDisplayItem("EUR €")
        SettingsActionItem("Leave a review", "", Icons.Default.Star) {}
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(15.dp)) {
            Icon(Icons.Default.RemoveCircleOutline, null, tint = Color.Red); Text("Log Out", color = Color.Red)
        }
    }
}
