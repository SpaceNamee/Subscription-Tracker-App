package com.ims.activesubscriptionsapp.ui.screens.auth
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ims.activesubscriptionsapp.ui.components.LocationPermission
import com.ims.activesubscriptionsapp.ui.components.SetLocation
import com.ims.activesubscriptionsapp.ui.screens.subscriptions.SubscriptionViewModel
val SlateBlue = Color(0xFF5680A0)
val LightGrayInput = Color(0xFFF6F7F9)
val TextGray = Color(0xFF6B7280)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    subscriptionViewModel: SubscriptionViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToNewPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val loginState by loginViewModel.loginState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showLocation by remember { mutableStateOf(false) }
    var showSetLocation by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            text = "The easiest way to manage your subscriptions.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        //INPUT EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        //INPUT PASSWORD
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        //STATUS LOGIN
        if (loginState == "Logging in...") {
            CircularProgressIndicator(color = SlateBlue, modifier = Modifier.size(30.dp))
        } else if (loginState.startsWith("Error")) {
            Text(text = loginState, color = Color.Red, fontSize = 14.sp, textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(24.dp))

        //BUTTON LOGIN
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    loginViewModel.performLogin(
                        email = email,
                        pass = password,
                        subscriptionViewModel = subscriptionViewModel,
                        onSuccess = { onLoginSuccess() }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SlateBlue),
            enabled = loginState != "Logging in..."
        ) {
            Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        //LINK REGISTER
        Row {
            Text(text = "Don't have an account? ")
            Text(
                text = "Register for FREE",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
    if (showLocation) {
        LocationPermission(
            onDismiss = { showLocation = false },
            onUseCurrentLocation = {
                showLocation = false
                loginViewModel.performLogin(
                    email = email,
                    pass = password,
                    subscriptionViewModel = subscriptionViewModel,
                    onSuccess = { onLoginSuccess() }
                )
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
                loginViewModel.performLogin(
                    email = email,
                    pass = password,
                    subscriptionViewModel = subscriptionViewModel,
                    onSuccess = { onLoginSuccess() }
                )
            }
        )
    }
}
