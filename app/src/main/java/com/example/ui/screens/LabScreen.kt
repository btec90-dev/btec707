package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.LabResult
import com.example.ui.theme.*
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabScreen(
    labResults: List<LabResult>,
    onAddLabResult: (String, String, Float, String) -> Unit,
    onDeleteLabResult: (String) -> Unit
) {
    // Firebase Storage upload states
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            isUploading = true
            uploadError = null
            val storageRef = Firebase.storage.reference.child("lab_reports/${System.currentTimeMillis()}.jpg")
            storageRef.putFile(uri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    uploadedImageUrl = downloadUri.toString()
                    isUploading = false
                }.addOnFailureListener { e ->
                    uploadError = e.localizedMessage
                    isUploading = false
                }
            }.addOnFailureListener { e ->
                uploadError = e.localizedMessage
                isUploading = false
            }
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "SUIVI BIOLOGIQUE & HORMONAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MutedText,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Espace Laboratoire",
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                color = InkText
            )
            Text(
                text = "Enregistrez vos taux hormonaux et stockez vos justificatifs sur Firebase Storage.",
                fontSize = 12.sp,
                color = MutedText
            )
        }

        // Summary Lab Stats Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "✧ Récapitulatif Labo RMA",
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    LabBadge("🥚 42", "Prélevés", Modifier.weight(1f))
                    LabBadge("◉ 34", "Matures", Modifier.weight(1f))
                    LabBadge("❄ 8", "Blastocystes", Modifier.weight(1f))
                }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFEAF4ED),
                    border = BorderStroke(1.dp, Color(0xFFBFE0CB))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🧬", fontSize = 20.sp)
                        Column {
                            Text(text = "Mise à jour PGT aujourd'hui", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B7A4B))
                            Text(text = "Mise à jour des résultats PGT aujourd'hui : 5 des embryons sont revenus génétiquement normaux :)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = InkText)
                        }
                    }
                }
            }
        }

        // Firebase Storage Upload Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "☁️ Stockage Firebase Storage (Pièces jointes)",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
                Text(
                    text = "Téléversez vos ordonnances ou comptes-rendus de laboratoire :",
                    fontSize = 11.5.sp,
                    color = MutedText
                )

                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Text(text = if (isUploading) "Téléversement en cours..." else "📁 Choisir une photo d'examen", fontWeight = FontWeight.Bold, color = GoldPrimary)
                }

                if (uploadError != null) {
                    Text(text = "Erreur : $uploadError", color = Color(0xFFBA4A4A), fontSize = 11.sp)
                }

                if (uploadedImageUrl != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "✓ Fichier téléversé avec succès sur Firebase Storage", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                        AsyncImage(
                            model = uploadedImageUrl,
                            contentDescription = "Pièce jointe labo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }

        // List of Lab Results
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "HISTORIQUE DES RÉSULTATS",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MutedText,
                letterSpacing = 1.2.sp
            )

            labResults.forEach { res ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(text = res.date.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                            Text(text = res.testName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = InkText)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${res.value} ${res.unit}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                            OutlinedButton(
                                onClick = { onDeleteLabResult(res.id) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBA4A4A))
                            ) {
                                Text(text = "✕", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LabBadge(text: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFFFDFAF6), RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFEBDCCB), RoundedCornerShape(10.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
        Text(text = label, fontSize = 10.sp, color = MutedText, fontWeight = FontWeight.Bold)
    }
}
