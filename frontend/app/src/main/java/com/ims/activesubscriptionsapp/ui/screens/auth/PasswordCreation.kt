package com.ims.activesubscriptionsapp.ui.screens.auth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.ims.activesubscriptionsapp.data.remote.PasswordResetViewModel

@Composable
fun PasswordCreation(
    viewModel: PasswordResetViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.navigateToLogin) {
        if (viewModel.navigateToLogin) {
            viewModel.navigateToLogin = false
            onNavigateToLogin()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "Password restore",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Heading
            Text(
                text = "Set New\n Password",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "You can always change it later.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 2. New Password Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "New Password",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text("Enter new password", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = LightGrayInput,
                        unfocusedContainerColor = LightGrayInput,
                        focusedBorderColor = SlateBlue.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    singleLine = true,
                    visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (isNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle visibility", tint = Color.Gray)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Confirm Password Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Confirm Password",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Confirm your password", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = LightGrayInput,
                        unfocusedContainerColor = LightGrayInput,
                        focusedBorderColor = SlateBlue.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    singleLine = true,
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle visibility", tint = Color.Gray)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (viewModel.errorMessage != null || passwordError != null) {
                Text(
                    text = viewModel.errorMessage ?: passwordError ?: "",
                    color = Color.Red,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 4. Save Button
            Button(
                onClick = {
                    if (newPassword != confirmPassword) {
                        passwordError = "Passwords do not match."
                    } else {
                        passwordError = null
                        viewModel.onResetPasswordClick(newPassword)
                        onNavigateToLogin()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SlateBlue)
            ) {
                Text("Change Password", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Need help?",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Write Us",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
        }
    }
}