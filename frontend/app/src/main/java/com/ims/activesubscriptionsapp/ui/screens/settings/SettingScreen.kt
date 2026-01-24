package com.ims.activesubscriptionsapp.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.ui.screens.auth.AuthStepScreen
import com.ims.activesubscriptionsapp.ui.components.SettingsActionItem
import com.ims.activesubscriptionsapp.ui.screens.auth.SetPasswordScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.SuccessScreen
import com.ims.activesubscriptionsapp.ui.screens.auth.VerificationCodeScreen
import com.ims.activesubscriptionsapp.ui.components.CurrencyDisplayItem

@Composable
fun SettingsFlowManager(
    initialEmail: String,
    flow: String,
    onNavigate: (String) -> Unit,
    onExit: () -> Unit,
    onLogout: () -> Unit // <--- NOVO: Parâmetro para lidar com a saída
) {
    // FIX: Usamos remember(initialEmail) para que o estado atualize se o email de login mudar
    var userEmail by remember(initialEmail) { mutableStateOf(initialEmail) }
    var pendingEmail by remember { mutableStateOf("") }

    when (flow) {
        "main" -> SettingsScreen(
            currentEmail = userEmail,
            onBack = onExit,
            onEmail = { onNavigate("email1") },
            onPass = { onNavigate("pass1") },
            onLogout = onLogout // <--- Passa a função para o ecrã
        )

        "email1" -> AuthStepScreen(
            title = "Type your new email",
            label = "Email",
            onBack = { onNavigate("main") },
            onNext = { emailDigitado ->
                pendingEmail = emailDigitado
                onNavigate("email2")
            }
        )

        "email2" -> VerificationCodeScreen(
            onBack = { onNavigate("email1") },
            onNext = {
                userEmail = pendingEmail
                onNavigate("emailSuccess")
            }
        )

        "emailSuccess" -> SuccessScreen("Email Changed Successfully", onDone = { onNavigate("main") })

        "pass1" -> AuthStepScreen("Email Verification", "Email", onBack = { onNavigate("main") }, onNext = { onNavigate("pass2") })
        "pass2" -> VerificationCodeScreen(onBack = { onNavigate("pass1") }, onNext = { onNavigate("pass3") })
        "pass3" -> SetPasswordScreen(onBack = { onNavigate("pass2") }, onNext = { onNavigate("passSuccess") })
        "passSuccess" -> SuccessScreen("Password Changed Successfully", onDone = { onNavigate("main") })
    }
}

@Composable
fun SettingsScreen(
    currentEmail: String,
    onBack: () -> Unit,
    onEmail: () -> Unit,
    onPass: () -> Unit,
    onLogout: () -> Unit // <--- Parâmetro adicionado
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.background(Color.White, CircleShape)) {
                Icon(Icons.Default.ArrowBack, null)
            }
            Text(
                "Settings",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        Text("Account settings", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 24.dp))

        SettingsActionItem("Email", currentEmail, Icons.Default.Email, onEmail)
        SettingsActionItem("Password", "********", Icons.Default.Lock, onPass)

        Text("Other info", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 16.dp))
        CurrencyDisplayItem("EUR €")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLogout, // <--- Agora o botão chama a função de saída
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(15.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Icon(Icons.Default.RemoveCircleOutline, null, tint = Color.Red)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Out", color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}