package com.ims.activesubscriptionsapp.ui.screens.auth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun SetPasswordScreen(onBack: () -> Unit, onNext: () -> Unit) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.background(Color(0xFFF2F2F7), CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, null)
        }
        Text(
            text = "Set new Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = "Enter your new password and confirm it below.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        //Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        //Confirmation
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Repeat Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            isError = confirmPassword.isNotEmpty() && password != confirmPassword
        )
        if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            Text(
                "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onNext,
            enabled = password.isNotEmpty() && password == confirmPassword,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .navigationBarsPadding(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5387AC),
                disabledContainerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("Change Password", color = Color.White)
        }
    }
}