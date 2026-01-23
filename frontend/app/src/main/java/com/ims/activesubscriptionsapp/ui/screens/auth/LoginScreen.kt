package com.ims.activesubscriptionsapp.ui.screens.auth
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ims.activesubscriptionsapp.ui.components.LocationPermission
import com.ims.activesubscriptionsapp.ui.components.SetLocation
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// Custom colors (Mantendo os seus)
val SlateBlue = Color(0xFF5680A0)
val LightGrayInput = Color(0xFFF6F7F9)
val TextGray = Color(0xFF6B7280)
/*
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToNewPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    // 1. OBSERVAR O ESTADO DO VIEWMODEL
    val state by viewModel.loginState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showLocation by remember { mutableStateOf(false) }
    var showSetLocation by remember { mutableStateOf(false) }

    // 2. LÓGICA DE NAVEGAÇÃO AUTOMÁTICA
    // Este bloco executa sempre que o 'state' muda.
    LaunchedEffect(state) {
        if (state.contains("Success")) {
            onLoginSuccess() // Fecha o login e abre o MainNavigation no MainActivity
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Spacer(modifier = Modifier.height(80.dp))

        // Header
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 35.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "The easiest way to rent an Umbrella on the beach.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campos de Texto (E-mail e Password omitidos para brevidade, mantenha os seus)
        // ... (Seu código de OutlinedTextField aqui) ...
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Type Email here", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LightGrayInput,
                unfocusedContainerColor = LightGrayInput,
                disabledContainerColor = LightGrayInput,
                focusedBorderColor = SlateBlue.copy(alpha = 0.5f),
                unfocusedBorderColor = Color.Transparent, // No border when not focused
            ),
            singleLine = true
        )
    }

        // 3. INDICADOR DE CARREGAMENTO E MENSAGEM DE ERRO
        Spacer(modifier = Modifier.height(16.dp))
        if (state.contains("Logging in")) {
            CircularProgressIndicator(color = SlateBlue, modifier = Modifier.size(30.dp))
        } else if (state.contains("Error")) {
            Text(text = state, color = Color.Red, fontSize = 14.sp, textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Login
        Button(
            onClick = { showLocation = true },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SlateBlue),
            enabled = !state.contains("Logging in") // Desativa enquanto carrega
        ) {
            Text("Login", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        // ... (Seu código do Google Button e Footer aqui) ...
    // 6. Google Button (Outlined)
    OutlinedButton(
        onClick = { /* TODO: Google Login */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
    ) {


        Text("G", fontWeight = FontWeight.Bold, color = Color.Red, modifier = Modifier.padding(end = 8.dp))

        Text("Continue with Google", fontSize = 16.sp, color = Color.Gray)
    }

    // 7. Footer text

    Spacer(modifier = Modifier.height(40.dp))

    Text(
        text = "Don't have an account? ",
        color = Color.Black,
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )
    Text(
        text = "Register for FREE",
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 17.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.clickable { onNavigateToRegister() }
    )
        // DIÁLOGOS DE LOCALIZAÇÃO
        if (showLocation) {
            LocationPermission(
                onDismiss = { showLocation = false },
                onUseCurrentLocation = {
                    showLocation = false
                    viewModel.performLogin(email, password) // DISPARA O LOGIN
                },
                onSetLocationClick = {
                    showLocation = false
                    showSetLocation = true
                }
            )
        }

        if (showSetLocation) {
            SetLocation(
                onDismiss = { showSetLocation = false },
                onBack = {
                    showSetLocation = false
                    showLocation = true
                },
                onSaveLocation = { location ->
                    showSetLocation = false
                    viewModel.performLogin(email, password) // DISPARA O LOGIN
                }
            )
        }
    }*/
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToNewPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.loginState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showLocation by remember { mutableStateOf(false) }
    var showSetLocation by remember { mutableStateOf(false) }

    // NAVEGAÇÃO INSTANTÂNEA
    LaunchedEffect(state) {
        // Usamos "Success" exato para evitar atrasos de processamento de string
        if (state == "Success") {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()), // Adicionado para evitar quebras em ecrãs pequenos
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- HEADER ---
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 35.sp
            )
        )
        Text(
            text = "The easiest way to manage your subscriptions.",
            color = TextGray, textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- CAMPOS DE INPUT ---
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        // --- STATUS & LOADING ---
        Spacer(modifier = Modifier.height(16.dp))
        if (state == "Logging in...") {
            CircularProgressIndicator(color = SlateBlue, modifier = Modifier.size(30.dp))
        } else if (state.startsWith("Error")) {
            Text(text = state, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOTÃO LOGIN ---
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    showLocation = true
                } else {
                    // Feedback rápido se campos vazios
                    viewModel.performLogin(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SlateBlue),
            enabled = state != "Logging in..."
        ) {
            Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- REGISTAR ---
        Row {
            Text(text = "Don't have an account? ")
            Text(
                text = "Register for FREE",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }

    // --- LÓGICA DE DIÁLOGOS (Fora da Column principal) ---
    if (showLocation) {
        LocationPermission(
            onDismiss = { showLocation = false },
            onUseCurrentLocation = {
                showLocation = false
                viewModel.performLogin(email, password)
            },
            onSetLocationClick = {
                showLocation = false
                showSetLocation = true
            }
        )
    }

    if (showSetLocation) {
        SetLocation(
            onDismiss = { showSetLocation = false },
            onBack = { showSetLocation = false; showLocation = true },
            onSaveLocation = { _ ->
                showSetLocation = false
                viewModel.performLogin(email, password)
            }
        )
    }
}

