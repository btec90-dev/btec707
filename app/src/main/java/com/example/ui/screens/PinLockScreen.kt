package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PinLockScreen(
    onUnlockSuccess: () -> Unit
) {
    var enteredPin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val correctPin = "1998"

    fun onNumberClick(digit: String) {
        if (enteredPin.length < 4) {
            enteredPin += digit
            showError = false
            if (enteredPin.length == 4) {
                if (enteredPin == correctPin) {
                    onUnlockSuccess()
                } else {
                    showError = true
                    enteredPin = ""
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 360.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "🔒", fontSize = 34.sp)
                Text(
                    text = "Mon Parcours",
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = InkText
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFF4EBD8),
                    border = BorderStroke(1.dp, BorderLine)
                ) {
                    Text(
                        text = "🔒 Espace Sécurisé · Fatou",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5C3B24)
                    )
                }

                Text(
                    text = "Entrez votre code PIN secret",
                    fontSize = 12.5.sp,
                    color = MutedText,
                    textAlign = TextAlign.Center
                )

                // PIN Dots Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    for (i in 0 until 4) {
                        val isFilled = i < enteredPin.length
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) GoldPrimary else Color(0xFFE2D3C4))
                                .border(1.dp, if (isFilled) GoldPrimary else BorderLine, CircleShape)
                        )
                    }
                }

                if (showError) {
                    Text(
                        text = "Code PIN incorrect",
                        color = Color(0xFFBA4A4A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Keypad grid optimized for mobile touch targets
                val buttons = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("", "0", "⌫")
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    buttons.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { btnText ->
                                if (btnText.isEmpty()) {
                                    Spacer(modifier = Modifier.size(60.dp))
                                } else {
                                    Button(
                                        onClick = {
                                            if (btnText == "⌫") {
                                                if (enteredPin.isNotEmpty()) {
                                                    enteredPin = enteredPin.dropLast(1)
                                                    showError = false
                                                }
                                            } else {
                                                onNumberClick(btnText)
                                            }
                                        },
                                        modifier = Modifier.size(60.dp),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFDFAF6),
                                            contentColor = InkText
                                        ),
                                        border = BorderStroke(1.dp, BorderLine),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = btnText,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
