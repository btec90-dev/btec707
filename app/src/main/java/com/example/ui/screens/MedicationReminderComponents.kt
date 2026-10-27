package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.*
import com.example.util.MedicationReminderScheduler
import com.example.util.ScheduledReminderInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScheduleReminderModal(
    event: CalendarEventDetail,
    onDismiss: () -> Unit,
    onReminderScheduled: () -> Unit
) {
    val context = LocalContext.current
    val eventId = "evt_${event.month}_${event.day}_${event.time.filter { it.isDigit() }}"

    // Delay presets (seconds, label)
    val options = listOf(
        Pair(10L, "Test immédiat WorkManager (dans 10 secondes)"),
        Pair(900L, "15 minutes avant l'heure (${event.time})"),
        Pair(3600L, "1 heure avant l'injection (${event.time})"),
        Pair(7200L, "2 heures avant pour la préparation du matériel"),
        Pair(28800L, "Rappel matinal du protocole (dans 8 heures)")
    )

    var selectedDelay by remember { mutableStateOf(options[0].first) }
    var selectedLabel by remember { mutableStateOf(options[0].second) }
    var permissionDeniedNotice by remember { mutableStateOf(false) }

    fun scheduleAndFinish() {
        val reminderTitle = "Rappel FIV : ${event.title}"
        val reminderDetails = "⏰ Prévu à ${event.time} — ${event.details}"
        MedicationReminderScheduler.scheduleReminder(
            context = context,
            eventId = eventId,
            title = reminderTitle,
            details = reminderDetails,
            delaySeconds = selectedDelay,
            timeLabel = selectedLabel
        )
        Toast.makeText(
            context,
            "Rappel WorkManager programmé avec succès !",
            Toast.LENGTH_SHORT
        ).show()
        onReminderScheduled()
        onDismiss()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            scheduleAndFinish()
        } else {
            permissionDeniedNotice = true
            Toast.makeText(
                context,
                "Permission requise pour afficher les notifications de rappel.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun handleScheduleClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                scheduleAndFinish()
            } else {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            scheduleAndFinish()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🔔", fontSize = 20.sp)
                    Text(
                        text = "Programmer un rappel WorkManager",
                        fontSize = 17.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = InkText
                    )
                }
                Text(
                    text = "${event.title} (${event.day} sept. à ${event.time})",
                    fontSize = 12.sp,
                    color = GoldPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Choisissez quand déclencher la notification en arrière-plan via Android WorkManager :",
                    fontSize = 12.sp,
                    color = MutedText
                )

                options.forEach { (delay, label) ->
                    val isSelected = selectedDelay == delay
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                selectedDelay = delay
                                selectedLabel = label
                            },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFFFBF4EB) else Color(0xFFF9F9F9),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) GoldPrimary else Color(0xFFE5DDD5)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    selectedDelay = delay
                                    selectedLabel = label
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = GoldPrimary
                                )
                            )
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) InkText else Color(0xFF5A4D45)
                            )
                        }
                    }
                }

                if (permissionDeniedNotice) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFDEDED),
                        border = BorderStroke(1.dp, Color(0xFFF5C6CB))
                    ) {
                        Text(
                            text = "Veuillez autoriser les notifications dans les paramètres Android pour recevoir les alertes d'injection.",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 11.sp,
                            color = Color(0xFF8B252E)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { handleScheduleClick() },
                modifier = Modifier.testTag("confirm_schedule_reminder_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Activer le rappel", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Annuler", fontSize = 12.sp, color = MutedText)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ActiveRemindersSection(
    modifier: Modifier = Modifier,
    onRemindersUpdated: () -> Unit = {}
) {
    val context = LocalContext.current
    var activeReminders by remember {
        mutableStateOf(MedicationReminderScheduler.getAllScheduledReminders(context))
    }
    var showTestScheduleModal by remember { mutableStateOf(false) }

    fun refreshList() {
        activeReminders = MedicationReminderScheduler.getAllScheduledReminders(context)
        onRemindersUpdated()
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F5)),
        border = BorderStroke(1.dp, BorderLine),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF0E5D8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⏰", fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = "Rappels Injections WorkManager",
                            fontSize = 14.5.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = InkText
                        )
                        Text(
                            text = "Notifications locales fiables en tâche de fond",
                            fontSize = 10.5.sp,
                            color = MutedText
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (activeReminders.isNotEmpty()) Color(0xFFEAF4ED) else Color(0xFFF0EDE9)
                ) {
                    Text(
                        text = "${activeReminders.size} actif${if (activeReminders.size > 1) "s" else ""}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeReminders.isNotEmpty()) SuccessGreen else MutedText
                    )
                }
            }

            if (activeReminders.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFEFE8E0))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Aucun rappel en attente. Vous pouvez activer un rappel sur n'importe quelle étape du calendrier ou lancer un test immédiat ci-dessous.",
                            fontSize = 11.5.sp,
                            color = MutedText,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Button(
                            onClick = {
                                val dummyEvent = CalendarEventDetail(
                                    day = 21,
                                    month = 9,
                                    time = "20 h 00",
                                    title = "Injection Gonal-F 225 UI + Menopur 75 UI",
                                    location = "Domicile",
                                    details = "Injection sous-cutanée péri-ombilicale à heure fixe",
                                    category = "Stimulation"
                                )
                                MedicationReminderScheduler.scheduleReminder(
                                    context = context,
                                    eventId = "evt_9_21_quicktest",
                                    title = "Rappel FIV : ${dummyEvent.title}",
                                    details = "⏰ Prévu à ${dummyEvent.time} — ${dummyEvent.details}",
                                    delaySeconds = 10L,
                                    timeLabel = "Test immédiat WorkManager (10s)"
                                )
                                Toast.makeText(context, "Test WorkManager programmé (alerte dans 10s)", Toast.LENGTH_SHORT).show()
                                refreshList()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("test_workmanager_quick_button")
                        ) {
                            Text(text = "🔔 Lancer un test d'injection (dans 10s)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    activeReminders.forEach { reminder ->
                        val dateFormatted = remember(reminder.triggerTimestampMillis) {
                            if (reminder.triggerTimestampMillis > 0) {
                                SimpleDateFormat("HH:mm:ss", Locale.FRANCE).format(Date(reminder.triggerTimestampMillis))
                            } else "Programmé"
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFE8DFD5))
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = "🔔", fontSize = 12.sp)
                                        Text(
                                            text = reminder.title,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = InkText
                                        )
                                    }
                                    Text(
                                        text = "${reminder.timeLabel} • Prévu à ~$dateFormatted",
                                        fontSize = 10.5.sp,
                                        color = GoldSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = reminder.details,
                                        fontSize = 10.sp,
                                        color = MutedText,
                                        maxLines = 1
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        MedicationReminderScheduler.cancelReminder(context, reminder.eventId)
                                        Toast.makeText(context, "Rappel annulé", Toast.LENGTH_SHORT).show()
                                        refreshList()
                                    },
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF9E4747))
                                ) {
                                    Text(text = "Annuler", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                MedicationReminderScheduler.cancelAllReminders(context)
                                Toast.makeText(context, "Tous les rappels ont été effacés", Toast.LENGTH_SHORT).show()
                                refreshList()
                            }
                        ) {
                            Text(
                                text = "Tout annuler",
                                fontSize = 11.sp,
                                color = Color(0xFF8B252E),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
