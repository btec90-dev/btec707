package com.example.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.example.ui.theme.*
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "♡", fontSize = 32.sp, color = GoldPrimary)
                Text(
                    text = "Mon Parcours FIV",
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = InkText
                )
                Text(
                    text = if (isSignUp) "Créer un compte sécurisé" else "Connexion à votre espace",
                    fontSize = 13.sp,
                    color = MutedText
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Adresse email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mot de passe") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFBA4A4A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            errorMessage = null
                            coroutineScope.launch {
                                try {
                                    val auth = Firebase.auth
                                    if (isSignUp) {
                                        auth.createUserWithEmailAndPassword(email.trim(), password).await()
                                    } else {
                                        auth.signInWithEmailAndPassword(email.trim(), password).await()
                                    }
                                    isLoading = false
                                    onAuthSuccess()
                                } catch (e: Exception) {
                                    isLoading = false
                                    errorMessage = e.localizedMessage ?: "Erreur d'authentification"
                                }
                            }
                        } else {
                            errorMessage = "Veuillez remplir tous les champs"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text(text = if (isSignUp) "S'inscrire" else "Se connecter", fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSignUp) "Déjà un compte ?" else "Pas encore de compte ?",
                        fontSize = 12.sp,
                        color = MutedText
                    )
                    TextButton(onClick = { isSignUp = !isSignUp }) {
                        Text(
                            text = if (isSignUp) "Se connecter" else "S'inscrire",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                    }
                }

                Divider(color = BorderLine)

                // Google Sign-In fallback
                OutlinedButton(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        val clientId = context.getString(R.string.default_web_client_id)
                        val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
                        val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

                        coroutineScope.launch {
                            try {
                                val result = credentialManager.getCredential(context as Activity, request)
                                val credential = result.credential
                                if (credential is androidx.credentials.CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                                    val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                                    Firebase.auth.signInWithCredential(authCredential).await()
                                    isLoading = false
                                    onAuthSuccess()
                                } else {
                                    isLoading = false
                                    errorMessage = "Type de credential inattendu"
                                }
                            } catch (e: GetCredentialCancellationException) {
                                isLoading = false
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = e.localizedMessage ?: "Échec de la connexion Google"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, GoldPrimary)
                ) {
                    Text(text = "Connexion avec Google", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
