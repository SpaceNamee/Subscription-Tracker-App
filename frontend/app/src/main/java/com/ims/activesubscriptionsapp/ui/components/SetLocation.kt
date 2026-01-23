package com.ims.activesubscriptionsapp.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ims.activesubscriptionsapp.ui.theme.SlateBlue
import com.ims.activesubscriptionsapp.ui.theme.LightGrayInput

@Composable
fun SetLocation(
    onDismiss: () -> Unit,
    onSaveLocation: (String) -> Unit, // Callback with the entered location
    onBack: () -> Unit,
) {
    // State for the input text field
    var locationQuery by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Title
                Text(
                    text = "Location Sharing",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Divider
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Description
                Text(
                    text = "In order to provide you with the best experience and to ensure accurate results, we need to know your location. You can use your Current Location or set it Manually.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )

                Spacer(modifier = Modifier.height(40.dp))

                // 4. Custom Location Input Field
                Text(
                    text = "Set your Location",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = locationQuery,
                    onValueChange = { locationQuery = it },
                    placeholder = { Text("Choose your location...", color = Color.Gray) },
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
                    // Leading Icon: Location Pin
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = SlateBlue
                        )
                    },
                    // Trailing Icon: 'X' to clear text (only shows when text is not empty)
                    trailingIcon = {
                        if (locationQuery.isNotEmpty()) {
                            IconButton(onClick = { locationQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Clear text",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 5. Save Location Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, // Pushes buttons to edges
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Back Button (Left)
                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                    ) {
                        Text(
                            text = "Back",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // 2. Save Button (Right)
                    Button(
                        onClick = {
                            if (locationQuery.isNotBlank()) {
                                onSaveLocation(locationQuery)
                            }
                        },
                        // Use weight(1f) if you want it to fill the remaining space,
                        // or remove modifier to keep it regular size.
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SlateBlue),
                        enabled = locationQuery.isNotBlank()
                    ) {
                        Text(
                            text = "Done",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}