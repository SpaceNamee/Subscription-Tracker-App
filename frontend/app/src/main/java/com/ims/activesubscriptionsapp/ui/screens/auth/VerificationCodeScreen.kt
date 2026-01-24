package com.ims.activesubscriptionsapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerificationCodeScreen(onBack: () -> Unit, onNext: () -> Unit) {
    // Lista para guardar os 6 dígitos
    val codeValues = remember { mutableStateListOf("", "", "", "", "", "") }
    // Lista para controlar o foco automático entre as caixas
    val focusRequesters = remember { List(6) { FocusRequester() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding() // Resolve o problema do notch no Pixel
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color(0xFFF2F2F7), CircleShape)
            ) { Icon(Icons.Default.ArrowBack, null) }
        }

        Text(
            "Check your Email",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            "Enter the 6-digit code sent to you",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Linha com os campos de Input reais
        Row(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            codeValues.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { input ->
                        if (input.length <= 1) {
                            codeValues[index] = input
                            // Salto automático para a direita
                            if (input.isNotEmpty() && index < 5) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .focusRequester(focusRequesters[index]),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF5387AC),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            // Só ativa o botão quando todos os 6 campos estiverem preenchidos
            enabled = codeValues.all { it.isNotEmpty() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .navigationBarsPadding(), // Margem de segurança inferior
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5387AC),
                disabledContainerColor = Color.LightGray
            )
        ) {
            Text("Verify", color = Color.White)
        }
    }

    // Abre o teclado automaticamente na primeira caixa ao entrar no ecrã
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}