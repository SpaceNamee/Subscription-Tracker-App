package com.ims.activesubscriptionsapp.ui.screens.auth
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ims.activesubscriptionsapp.data.remote.PasswordResetViewModel

@Composable
fun CodeScreen(
    viewModel: PasswordResetViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReset: () -> Unit
) {
    var code by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.navigateToReset) {
        if (viewModel.navigateToReset) {
            viewModel.navigateToReset = false
            onNavigateToReset()
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
                text = "Check your Email",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 32.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Instructions
            Text(
                text = "And Write down the 6-Digit Security Code we have sent you.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "6-Digit Security Code",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.DarkGray,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 3. 6-Digit Code Input
            BasicTextField(
                value = viewModel.code,
                onValueChange = { if (it.length <= 6) viewModel.code = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until 6) {
                            val char = if (i < viewModel.code.length) viewModel.code[i].toString() else ""
                            val isFocused = viewModel.code.length == i

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f) // Makes it square
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(LightGrayInput)
                                    .border(
                                        border = BorderStroke(2.dp, SlateBlue),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    ),
                                    color = Color.Black
                                )
                            }

                            if (i < 5) {
                                Text(
                                    text = "-",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // 4. "Check the Code" Button
            Button(
                onClick = { viewModel.onVerifyCodeClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SlateBlue),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Check the Code", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                //Text("Check the Code", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. "Send to different Email" Button
            OutlinedButton(
                onClick = { onNavigateBack() }, // Go back to change email
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SlateBlue),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SlateBlue)
            ) {
                Text("Send to different Email", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(60.dp))

            // 6. Footer - Resend

            Text(
                text = "Didn't get the Code?\n ",
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Resend",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { /* TODO: Resend logic */ }
            )

        }
    }

}