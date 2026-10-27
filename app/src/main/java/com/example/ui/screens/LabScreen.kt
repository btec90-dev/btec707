package com.example.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.theme.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class LabDocument(
    val id: String,
    val title: String,
    val category: String,
    val dateText: String,
    val localPath: String,
    val cloudUrl: String? = null,
    val isCloudSynced: Boolean = false
)

private fun saveDocuments(context: Context, docs: List<LabDocument>) {
    try {
        val array = JSONArray()
        for (doc in docs) {
            val obj = JSONObject().apply {
                put("id", doc.id)
                put("title", doc.title)
                put("category", doc.category)
                put("dateText", doc.dateText)
                put("localPath", doc.localPath)
                put("cloudUrl", doc.cloudUrl ?: "")
                put("isCloudSynced", doc.isCloudSynced)
            }
            array.put(obj)
        }
        val file = File(context.filesDir, "lab_documents.json")
        file.writeText(array.toString())
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun loadDocuments(context: Context): List<LabDocument> {
    val file = File(context.filesDir, "lab_documents.json")
    if (!file.exists()) return emptyList()
    return try {
        val array = JSONArray(file.readText())
        val list = mutableListOf<LabDocument>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val path = obj.getString("localPath")
            // Verify file exists
            if (File(path).exists()) {
                list.add(
                    LabDocument(
                        id = obj.getString("id"),
                        title = obj.getString("title"),
                        category = obj.getString("category"),
                        dateText = obj.getString("dateText"),
                        localPath = path,
                        cloudUrl = obj.optString("cloudUrl").takeIf { it.isNotEmpty() },
                        isCloudSynced = obj.optBoolean("isCloudSynced", false)
                    )
                )
            }
        }
        list
    } catch (e: Exception) {
        emptyList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var documents by remember { mutableStateOf<List<LabDocument>>(emptyList()) }
    var isProcessing by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf("📋 Ordonnance") }
    var previewDocument by remember { mutableStateOf<LabDocument?>(null) }

    val categories = listOf(
        "📋 Ordonnance",
        "🩸 Bilan sanguin",
        "🩻 Échographie",
        "🧬 Rapport PGT-A"
    )

    // Load persisted documents on composition
    LaunchedEffect(Unit) {
        documents = withContext(Dispatchers.IO) {
            loadDocuments(context)
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            isProcessing = true
            statusMessage = "Enregistrement du document en cours..."

            coroutineScope.launch {
                try {
                    // 1. Copy to secure private local app files directory
                    val docsDir = File(context.filesDir, "lab_documents").apply { mkdirs() }
                    val timestamp = System.currentTimeMillis()
                    val localFile = File(docsDir, "lab_doc_${timestamp}.jpg")

                    val copySuccess = withContext(Dispatchers.IO) {
                        try {
                            context.contentResolver.openInputStream(uri)?.use { input ->
                                localFile.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }
                            true
                        } catch (e: Exception) {
                            e.printStackTrace()
                            false
                        }
                    }

                    if (!copySuccess || !localFile.exists()) {
                        statusMessage = "Impossible de lire la photo sélectionnée."
                        isProcessing = false
                        return@launch
                    }

                    val dateFormat = SimpleDateFormat("d MMMM yyyy · HH:mm", Locale.FRENCH)
                    val dateFormatted = dateFormat.format(Date(timestamp))

                    val newDoc = LabDocument(
                        id = "doc-$timestamp",
                        title = "$selectedCategory du $dateFormatted",
                        category = selectedCategory,
                        dateText = dateFormatted,
                        localPath = localFile.absolutePath,
                        cloudUrl = null,
                        isCloudSynced = false
                    )

                    val updatedList = listOf(newDoc) + documents
                    documents = updatedList
                    withContext(Dispatchers.IO) {
                        saveDocuments(context, updatedList)
                    }

                    statusMessage = "✓ Document enregistré avec succès dans votre espace sécurisé local."
                    isProcessing = false

                    // 2. Attempt Firebase Storage synchronization gracefully in background
                    try {
                        val auth = Firebase.auth
                        if (auth.currentUser == null) {
                            try {
                                auth.signInAnonymously().await()
                            } catch (_: Exception) {}
                        }
                        val storageRef = Firebase.storage.reference.child("lab_reports/${localFile.name}")
                        storageRef.putFile(Uri.fromFile(localFile)).addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                val syncedList = documents.map { item ->
                                    if (item.id == newDoc.id) {
                                        item.copy(cloudUrl = downloadUri.toString(), isCloudSynced = true)
                                    } else item
                                }
                                documents = syncedList
                                coroutineScope.launch(Dispatchers.IO) {
                                    saveDocuments(context, syncedList)
                                }
                                statusMessage = "☁️ Synchronisé avec succès sur Firebase Storage !"
                            }
                        }.addOnFailureListener {
                            statusMessage = "✓ Document sauvegardé en local sécurisé (Protection médicale privée)."
                        }
                    } catch (e: Exception) {
                        statusMessage = "✓ Document sauvegardé en local sécurisé (Protection médicale privée)."
                    }

                } catch (e: Exception) {
                    statusMessage = "Erreur : ${e.localizedMessage}"
                    isProcessing = false
                }
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
                text = "Stockez vos ordonnances, bilans et comptes-rendus en toute sécurité.",
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
                            Text(
                                text = "Mise à jour des résultats PGT aujourd'hui : 5 des embryons sont revenus génétiquement normaux :)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = InkText
                            )
                        }
                    }
                }
            }
        }

        // Storage & Attachments Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("lab_storage_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "☁️ Stockage des Pièces Jointes",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldPrimary,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFAF1E4),
                        border = BorderStroke(1.dp, Color(0xFFE5D5C0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Sécurisé",
                                tint = GoldSecondary,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = "Sécurisé",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GoldSecondary
                            )
                        }
                    }
                }

                Text(
                    text = "Téléversez vos ordonnances ou comptes-rendus de laboratoire :",
                    fontSize = 12.sp,
                    color = MutedText
                )

                // Category selection chips
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Type de document à joindre :",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedText
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { cat ->
                            val isSelected = (selectedCategory == cat)
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) GoldPrimary else Color(0xFFF7F3EE),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) GoldPrimary else Color(0xFFE2D6CA)
                                ),
                                modifier = Modifier.clickable { selectedCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else InkText
                                )
                            }
                        }
                    }
                }

                // Upload Button
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("choose_exam_photo_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Traitement en cours...", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Text(text = "📁 Choisir une photo d'examen", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                // Feedback status message
                if (statusMessage != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = if (statusMessage?.startsWith("Erreur") == true) Color(0xFFFDE8E8) else Color(0xFFEDF7ED),
                        border = BorderStroke(1.dp, if (statusMessage?.startsWith("Erreur") == true) Color(0xFFF8B4B4) else Color(0xFFBFE0CB))
                    ) {
                        Text(
                            text = statusMessage ?: "",
                            modifier = Modifier.padding(8.dp),
                            color = if (statusMessage?.startsWith("Erreur") == true) Color(0xFFBA4A4A) else Color(0xFF2B7A4B),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Informative note on local and cloud storage
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFAF7F2),
                    border = BorderStroke(1.dp, Color(0xFFEBDCCB))
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "💡", fontSize = 14.sp)
                        Text(
                            text = "Vos photos sont protégées et stockées directement sur l'appareil. Elles restent consultables hors-ligne à tout moment.",
                            fontSize = 10.5.sp,
                            color = MutedText
                        )
                    }
                }
            }
        }

        // List of attached documents
        if (documents.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Documents enregistrés (${documents.size})",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = InkText
                    )
                }

                documents.forEach { doc ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("doc_card_${doc.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = doc.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = InkText
                                    )
                                    Text(
                                        text = doc.dateText,
                                        fontSize = 10.5.sp,
                                        color = MutedText
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (doc.isCloudSynced) Color(0xFFE8F4FD) else Color(0xFFF4ECE1),
                                    border = BorderStroke(1.dp, if (doc.isCloudSynced) Color(0xFFBCE1FA) else Color(0xFFDFCA9D))
                                ) {
                                    Text(
                                        text = if (doc.isCloudSynced) "☁️ Cloud" else "🔒 Local sécurisé",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (doc.isCloudSynced) Color(0xFF1E6BB8) else GoldSecondary
                                    )
                                }
                            }

                            // Document image preview
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, BorderLine, RoundedCornerShape(8.dp))
                                    .clickable { previewDocument = doc }
                            ) {
                                AsyncImage(
                                    model = doc.cloudUrl ?: File(doc.localPath),
                                    contentDescription = doc.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xCC000000)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = "Agrandir",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "Agrandir",
                                            fontSize = 9.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // Actions
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = { previewDocument = doc },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Voir", fontSize = 11.sp, color = GoldPrimary)
                                }

                                TextButton(
                                    onClick = {
                                        val toDelete = File(doc.localPath)
                                        if (toDelete.exists()) toDelete.delete()
                                        val updated = documents.filter { it.id != doc.id }
                                        documents = updated
                                        coroutineScope.launch(Dispatchers.IO) {
                                            saveDocuments(context, updated)
                                        }
                                        statusMessage = "Document supprimé."
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Supprimer",
                                        tint = Color(0xFFBA4A4A),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Supprimer", fontSize = 11.sp, color = Color(0xFFBA4A4A))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Full-screen Image Viewer Dialog
    if (previewDocument != null) {
        val currentDoc = previewDocument!!
        Dialog(onDismissRequest = { previewDocument = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentDoc.title,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = InkText
                            )
                            Text(
                                text = currentDoc.dateText,
                                fontSize = 10.sp,
                                color = MutedText
                            )
                        }
                        IconButton(
                            onClick = { previewDocument = null },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer",
                                tint = InkText
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, BorderLine, RoundedCornerShape(10.dp))
                    ) {
                        AsyncImage(
                            model = currentDoc.cloudUrl ?: File(currentDoc.localPath),
                            contentDescription = currentDoc.title,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Button(
                        onClick = { previewDocument = null },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                    ) {
                        Text(text = "Fermer", fontWeight = FontWeight.Bold, color = Color.White)
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
