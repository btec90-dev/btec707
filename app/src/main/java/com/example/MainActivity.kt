package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.FivViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: FivViewModel = viewModel()
                val currentTab by viewModel.currentTab.collectAsState()
                val checklistItems by viewModel.checklistItems.collectAsState()
                val journalNotes by viewModel.journalNotes.collectAsState()
                val labResults by viewModel.labResults.collectAsState()

                var isUnlocked by remember { mutableStateOf(false) }
                var isGoldTheme by remember { mutableStateOf(false) }

                val headerBgColor = if (isGoldTheme) Color(0xFFB58E45) else Color(0xFF5C3B24)
                val headerBorderColor = if (isGoldTheme) Color(0xFFDFCA9D) else Color(0xFF754E32)
                val bottomIndicatorColor = if (isGoldTheme) Color(0xFFF0DEB4) else Color(0xFFF0E2D4)

                if (!isUnlocked) {
                    PinLockScreen(
                        onUnlockSuccess = { isUnlocked = true }
                    )
                } else {
                    val sisCompleted = checklistItems.find { it.id == "chk-sis" }?.isCompleted == false.not()
                    val hysteroCompleted = checklistItems.find { it.id == "chk-hystero" }?.isCompleted == false.not()

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            // Top Mobile Bar with BROWN header (or Gold in Gold mode)
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .statusBarsPadding()
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = headerBgColor,
                                shadowElevation = 4.dp,
                                border = BorderStroke(1.dp, headerBorderColor)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "♡", fontSize = 22.sp, color = Color(0xFFFFDF7D))
                                        Column {
                                            Text(
                                                text = "Mon Parcours",
                                                fontSize = 16.sp,
                                                fontFamily = FontFamily.Serif,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFFFFF),
                                                maxLines = 1
                                            )
                                            Text(
                                                text = "Fatou ♥ · RMA East Side",
                                                fontSize = 10.5.sp,
                                                color = if (isGoldTheme) Color(0xFFFFF4D6) else Color(0xFFF0DEC9),
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Theme Switcher pill (Fatou / Or)
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = if (isGoldTheme) Color(0xFF8F6E2A) else Color(0xFF452B18),
                                            border = BorderStroke(1.dp, if (isGoldTheme) Color(0xFFD4B46E) else Color(0xFF8B5E39)),
                                            modifier = Modifier.clickable { isGoldTheme = !isGoldTheme }
                                        ) {
                                            Text(
                                                text = if (isGoldTheme) "✨ Or" else "🤎 Fatou",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color(0xFFFFFFFF)
                                            )
                                        }

                                        // PDF Export button
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = if (isGoldTheme) Color(0xFF5A4418) else Color(0xFF3B2312),
                                            border = BorderStroke(1.dp, if (isGoldTheme) Color(0xFFDFCA9D) else Color(0xFF8D6648)),
                                            modifier = Modifier.clickable {
                                                com.example.util.PdfHelper.generateAndSharePdf(this@MainActivity, checklistItems)
                                            }
                                        ) {
                                            Text(
                                                text = "📄 PDF",
                                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFFFFF)
                                            )
                                        }

                                        // Lock action button
                                        OutlinedButton(
                                            onClick = { isUnlocked = false },
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFFFFF)),
                                            border = BorderStroke(1.dp, Color(0x66FFFFFF))
                                        ) {
                                            Text(text = "🔒 Verrouiller", fontSize = 9.5.sp)
                                        }
                                    }
                                }
                            }
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = if (isGoldTheme) Color(0xFFFCF9F2) else Color(0xFAF8F4EF),
                                tonalElevation = 8.dp
                            ) {
                                val items = listOf(
                                    Triple("home", "Accueil", "⌂"),
                                    Triple("calendar", "Calendrier", "▦"),
                                    Triple("lab", "Labo", "🧪"),
                                    Triple("checklist", "Checklist", "☑"),
                                    Triple("journal", "Journal", "✍")
                                )

                                items.forEach { (tabId, label, symbol) ->
                                    val selected = (currentTab == tabId)
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { viewModel.setTab(tabId) },
                                        icon = {
                                            Text(
                                                text = symbol,
                                                fontSize = 18.sp,
                                                color = if (selected) (if (isGoldTheme) Color(0xFF947230) else Color(0xFF5C3B24)) else Color(0xFF7E7169)
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = label,
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) (if (isGoldTheme) Color(0xFF947230) else Color(0xFF5C3B24)) else Color(0xFF7E7169)
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = bottomIndicatorColor
                                        )
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentTab) {
                                "home" -> HomeScreen(
                                    checklistItems = checklistItems,
                                    onToggleCheck = { id -> viewModel.toggleChecklistItem(id) },
                                    onNavigateTab = { tab -> viewModel.setTab(tab) }
                                )
                                "calendar" -> CalendarScreen(
                                    sisCompleted = sisCompleted,
                                    onToggleSis = { viewModel.toggleChecklistItem("chk-sis") },
                                    hysteroCompleted = hysteroCompleted,
                                    onToggleHystero = { viewModel.toggleChecklistItem("chk-hystero") }
                                )
                                "lab" -> LabScreen(
                                    labResults = labResults,
                                    onAddLabResult = { date, name, value, unit -> viewModel.addLabResult(date, name, value, unit) },
                                    onDeleteLabResult = { id -> viewModel.deleteLabResult(id) }
                                )
                                "checklist" -> ChecklistScreen(
                                    items = checklistItems,
                                    onToggleCheck = { id -> viewModel.toggleChecklistItem(id) }
                                )
                                "journal" -> JournalScreen(
                                    notes = journalNotes,
                                    onAddNote = { title, date, cat, content -> viewModel.addNote(title, date, cat, content) },
                                    onUpdateNote = { id, title, date, cat, content -> viewModel.updateNote(id, title, date, cat, content) },
                                    onDeleteNote = { id -> viewModel.deleteNote(id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
