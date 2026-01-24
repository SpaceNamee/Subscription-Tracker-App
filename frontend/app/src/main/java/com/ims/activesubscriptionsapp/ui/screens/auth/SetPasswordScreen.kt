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
    // ESTADOS: Criamos variáveis para "lembrar" o que o user escreve
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding() // FIX: Desce o botão de voltar para não bater na câmara/relógio do Pixel
            .padding(20.dp)
    ) {
        // Botão voltar com fundo para destaque
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

        // CAMPO 1: Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it }, // Atualiza a variável ao digitar
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        // CAMPO 2: Confirmação
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it }, // Atualiza a variável ao digitar
            label = { Text("Repeat Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            isError = confirmPassword.isNotEmpty() && password != confirmPassword
        )

        // Mensagem de erro se não forem iguais
        if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            Text(
                "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botão só ativa se as passwords forem iguais e não estiverem vazias
        Button(
            onClick = onNext,
            enabled = password.isNotEmpty() && password == confirmPassword,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .navigationBarsPadding(), // Margem de segurança para o fundo do ecrã
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