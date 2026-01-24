package com.ims.activesubscriptionsapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.* // Importante para o mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthStepScreen(
    title: String,
    label: String,
    onBack: () -> Unit,
    onNext: (String) -> Unit // Alterado para passar o texto capturado
) {
    // ESTADO: Isto guarda o que o utilizador escreve
    var textValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding() // FIX: Desce o botão de voltar no Pixel 7/8
            .padding(20.dp)
    ) {
        // Botão de voltar com um fundo suave para ser mais fácil de clicar
        IconButton(
            onClick = onBack,
            modifier = Modifier.background(Color(0xFFF2F2F7), CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, null)
        }

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 32.dp)
        )

        // CAMPO DE INPUT CORRIGIDO
        OutlinedTextField(
            value = textValue, // Usa a variável de estado
            onValueChange = { textValue = it }, // Atualiza a variável quando o user digita
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF5387AC),
                focusedLabelColor = Color(0xFF5387AC)
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botão Next
        Button(
            onClick = { onNext(textValue) },
            enabled = textValue.isNotEmpty(), // Só deixa avançar se tiver escrito algo
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .navigationBarsPadding(), // FIX: Não deixa o botão colado ao fundo do Pixel
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5387AC))
        ) {
            Text("Continue", color = Color.White)
        }
    }
}