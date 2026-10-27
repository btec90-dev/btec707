package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.JournalNote
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    notes: List<JournalNote>,
    onAddNote: (String, String, String, String) -> Unit,
    onUpdateNote: (String, String, String, String, String) -> Unit,
    onDeleteNote: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2026-10-28") }
    var category by remember { mutableStateOf("Émotion & Pensée") }
    var content by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }
    var expandedCategory by remember { mutableStateOf(false) }

    // Quick mood tracker state
    var selectedMood by remember { mutableStateOf("Pleine d'espoir ✨") }
    val moods = listOf("Sereine 🌸", "Pleine d'espoir ✨", "Fatou courage 🤍", "Fatiguée 💤", "Légère anxiété 💭")

    val categories = listOf("Émotion & Pensée", "Médical / Questions", "Jalon important")
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
                text = "ESPACE PERSONNEL & SUIVI DOUX",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MutedText,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Journal d’émotions & Mémos",
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                color = InkText
            )
            Text(
                text = "Notez vos ressentis, vos symptômes au quotidien et vos questions.",
                fontSize = 12.sp,
                color = MutedText
            )
        }

        // Daily Mood Quick Check-in Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Comment vous sentez-vous aujourd'hui, Fatou ?",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
                Text(
                    text = "Sélectionnez votre état d’esprit du jour :",
                    fontSize = 11.5.sp,
                    color = MutedText
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    moods.take(3).forEach { mood ->
                        MoodChip(
                            text = mood,
                            selected = (selectedMood == mood),
                            onClick = { selectedMood = mood },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    moods.drop(3).forEach { mood ->
                        MoodChip(
                            text = mood,
                            selected = (selectedMood == mood),
                            onClick = { selectedMood = mood },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Note Editor Card
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                        text = if (editingId != null) "✏️ Modifier le mémo" else "✍ Écrire un nouveau mémo",
                        fontSize = 17.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                    if (editingId != null) {
                        Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFF4E9DD)) {
                            Text(
                                text = "Mode édition",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }
                    }
                }

                Divider(color = BorderLine)

                // Title field
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "TITRE DU MÉMO", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MutedText)
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("ex. Ressenti après l'examen SIS...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(9.dp),
                        singleLine = true
                    )
                }

                // Date & Category row
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "DATE", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MutedText)
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(9.dp),
                            singleLine = true
                        )
                    }
                    Column(modifier = Modifier.weight(1.2f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "CATÉGORIE", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MutedText)
                        ExposedDropdownMenuBox(
                            expanded = expandedCategory,
                            onExpandedChange = { expandedCategory = !expandedCategory }
                        ) {
                            OutlinedTextField(
                                value = category,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(9.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expandedCategory,
                                onDismissRequest = { expandedCategory = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            category = cat
                                            expandedCategory = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Content field
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "VOS ÉMOTIONS & SYMPTÔMES", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MutedText)
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        placeholder = { Text("Écrivez ici vos ressentis, symptômes ou questions pour l'équipe RMA...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        shape = RoundedCornerShape(9.dp)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            if (title.isNotBlank() && content.isNotBlank()) {
                                val fullContent = "[$selectedMood] $content"
                                if (editingId != null) {
                                    onUpdateNote(editingId!!, title, date, category, fullContent)
                                    editingId = null
                                } else {
                                    onAddNote(title, date, category, fullContent)
                                }
                                title = ""
                                content = ""
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(9.dp)
                    ) {
                        Text(text = if (editingId != null) "Mettre à jour" else "💾 Enregistrer dans le journal", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = {
                            title = ""
                            content = ""
                            editingId = null
                        },
                        shape = RoundedCornerShape(9.dp)
                    ) {
                        Text(text = "Effacer")
                    }
                }
            }
        }

        // Notes List
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            notes.forEach { note ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val (catBg, catFg) = when (note.category) {
                                "Émotion & Pensée" -> Color(0xFFFDEBF0) to Color(0xFFAA4B64)
                                "Médical / Questions" -> Color(0xFFE7F0F8) to Color(0xFF2C5B7C)
                                else -> Color(0xFFE5F4EC) to Color(0xFF287454)
                            }
                            Surface(shape = RoundedCornerShape(12.dp), color = catBg) {
                                Text(
                                    text = note.category,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = catFg
                                )
                            }
                            Text(text = note.date, fontSize = 11.sp, color = MutedText)
                        }

                        Text(text = note.title, fontSize = 16.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = InkText)
                        Text(text = note.content, fontSize = 12.sp, color = Color(0xFF526068), lineHeight = 18.sp)

                        Divider(color = Color(0xFFEFE4D3), thickness = 1.dp)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                            OutlinedButton(
                                onClick = {
                                    editingId = note.id
                                    title = note.title
                                    date = note.date
                                    category = note.category
                                    content = note.content
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(text = "✏️ Modifier", fontSize = 10.5.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = { onDeleteNote(note.id) },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBA4A4A))
                            ) {
                                Text(text = "🗑 Supprimer", fontSize = 10.5.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoodChip(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .height(36.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = if (selected) GoldPrimary else Color(0xFFFDFAF6),
        border = BorderStroke(1.dp, if (selected) GoldPrimary else BorderLine)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else InkText
            )
        }
    }
}
