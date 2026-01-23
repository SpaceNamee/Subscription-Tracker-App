package com.ims.activesubscriptionsapp.ui.screens.auth
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.ims.activesubscriptionsapp.ui.components.LocationPermission
import com.ims.activesubscriptionsapp.ui.components.SetLocation

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToLogin: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isRepeatVisible by remember { mutableStateOf(false) }

    var showLocation by remember { mutableStateOf(false) }
    var showSetLocation by remember { mutableStateOf(false) }

    val statusMessage by viewModel.registerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // 1. Header
        Text(
            text = "Welcome to SubTrack",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 35.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create an Account for FREE",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Name Field (UI Only)
        RegisterField(
            label = "Name",
            value = name,
            onValueChange = { name = it },
            placeholder = "e.g. John Doe"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Email Field
        RegisterField(
            label = "Email",
            value = email,
            onValueChange = { email = it },
            placeholder = "e.g. example@gmail.com",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Password Field
        RegisterPasswordField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            isVisible = isPasswordVisible,
            onToggleVisibility = { isPasswordVisible = !isPasswordVisible }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Repeat Password Field
        RegisterPasswordField(
            label = "Repeat Password",
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            isVisible = isRepeatVisible,
            onToggleVisibility = { isRepeatVisible = !isRepeatVisible }
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 6. Register Button
        Button(
            onClick = { showLocation = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SlateBlue)
        ) {
            Text("Register", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Message
        if (statusMessage.isNotEmpty()) {
            Text(text = statusMessage, color = if(statusMessage.startsWith("Success")) SlateBlue else Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 7. Footer
        Text(
            text = "Already have an account? ",
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Login",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )

        if (showLocation) {
            LocationPermission(
                onDismiss = { showLocation = false },
                onUseCurrentLocation = {
                    showLocation = false
                    // Register here
                    viewModel.performRegister(email, password, repeatPassword)
                },
                onSetLocationClick = {
                    // Close this dialog and open the manual entry one
                    showLocation = false
                    showSetLocation = true
                }
            )
        }

        if (showSetLocation) {
            SetLocation(
                onDismiss = { showSetLocation = false },
                onBack = {
                    // Go back to the previous dialog
                    showSetLocation = false
                    showLocation = true
                },
                onSaveLocation = { location ->
                    showSetLocation = false
                    // Register
                    viewModel.performRegister(email, password, repeatPassword)
                }
            )
        }
    }
}



@Composable
fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
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
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Composable
fun RegisterPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("********", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LightGrayInput,
                unfocusedContainerColor = LightGrayInput,
                focusedBorderColor = SlateBlue.copy(alpha = 0.5f),
                unfocusedBorderColor = Color.Transparent,
            ),
            singleLine = true,
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = onToggleVisibility) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = Color.Gray)
                }
            }
        )
    }
}