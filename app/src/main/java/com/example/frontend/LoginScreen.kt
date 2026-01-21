package com.example.frontend

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.data.LoginViewModel

// Define custom colors
val SlateBlue = Color(0xFF5680A0)
val LightGrayInput = Color(0xFFF6F7F9)
val TextGray = Color(0xFF6B7280)

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel(),
                onNavigateToRegister: () -> Unit,
                onNavigateToNewPassword: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // State for password visibility toggle
    var isPasswordVisible by remember { mutableStateOf(false) }

    var showLocation by remember { mutableStateOf(false) }
    var showSetLocation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // 1. Header Section
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
            textAlign = TextAlign.Center,

        )

        Spacer(modifier = Modifier.height(40.dp))

        // 2. Email Field (Label outside)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Email",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

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

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Password Field (Label outside + Toggle Icon)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Password",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Type Password here", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = LightGrayInput,
                    unfocusedContainerColor = LightGrayInput,
                    disabledContainerColor = LightGrayInput,
                    focusedBorderColor = SlateBlue.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.Transparent,
                ),
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (isPasswordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = Color.Gray)
                    }
                }
            )
        }

        // 4. Forgot Password Link
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Forgot your Password",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = Color.DarkGray,
                modifier = Modifier.clickable { onNavigateToNewPassword() }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 5. Login Button
        Button(
            onClick = { showLocation = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SlateBlue)
        ) {
            Text("Login", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {

            val annotatedString = buildAnnotatedString {
                append("Don't have an Account?\n ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append("Register for FREE")
                }
            }

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp)) // Bottom padding

        if (showLocation) {
            LocationPermission(
                onDismiss = { showLocation = false },
                onUseCurrentLocation = {
                    showLocation = false
                    // Proceed with actual login logic here if needed
                    viewModel.performLogin(email, password)
                },
                onSetLocationClick = {
                    // Close the first dialog and open the second one
                    showLocation = false
                    showSetLocation = true
                }
            )
        }

        if (showSetLocation) {
            SetLocation(
                onDismiss = { showSetLocation = false },

                // Back button click
                onBack = {
                    showSetLocation = false // Close current
                    showLocation = true     // Re-open previous
                },

                onSaveLocation = { location ->
                    showSetLocation = false
                    println("Location saved: $location")
                    viewModel.performLogin(email, password)
                }
            )
        }

    }
}