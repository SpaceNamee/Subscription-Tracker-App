package com.example.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.LightGrayInput
import com.example.frontend.SlateBlue
import com.example.frontend.TextGray

@Composable
fun NewPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCode: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 16.dp), // Position the whole row
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Login",
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

            // 1. Header
            Text(
                text = "Forgot your Password?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Don't worry, we can restore it",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 2. Email Field
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
                    placeholder = { Text("Type email here", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = LightGrayInput,
                        unfocusedContainerColor = LightGrayInput,
                        disabledContainerColor = LightGrayInput,
                        focusedBorderColor = SlateBlue.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 3. Submit Button
            Button(
                onClick = {
                    onNavigateToCode()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SlateBlue)
            ) {
                Text("Send Code to Email", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Not what you were looking for?",
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