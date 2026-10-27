package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.util.MedicationReminderScheduler
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun GraduationCountdownCard(modifier: Modifier = Modifier) {
    // IVF Graduation milestone: weeks 8-10 post-transfer / gestation
    // FET target: Oct 31, 2026. Graduation week 9 ~ Dec 25, 2026.
    val fetDate = LocalDate.of(2026, 10, 31)
    val today = LocalDate.now()
    val daysSinceFet = ChronoUnit.DAYS.between(fetDate, today).coerceAtLeast(0)
    // Approximate gestational age at FET is ~2 weeks (or fertilization day 5 blastocyst + 2 weeks)
    val totalGestationalDays = daysSinceFet + 19 // ~2 weeks + 5 days at FET
    val currentWeeks = totalGestationalDays / 7
    val currentDaysRemainder = totalGestationalDays % 7
    val graduationTargetDays = 63 // Week 9 (63 days)
    val daysUntilGraduation = (graduationTargetDays - totalGestationalDays).coerceAtLeast(0)
    val progressFraction = (totalGestationalDays.toFloat() / graduationTargetDays).coerceIn(0f, 1f)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF6EE)),
        border = BorderStroke(1.dp, GoldPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF4EBD8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎓", fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = "Compte à rebours de Graduation",
                            fontSize = 14.5.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5C3B24)
                        )
                        Text(
                            text = "Transition clinique RMA vers l'OBGYN (Semaines 8–10)",
                            fontSize = 10.5.sp,
                            color = MutedText
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFEAF4ED),
                    border = BorderStroke(1.dp, Color(0xFFC7E2D0))
                ) {
                    Text(
                        text = "Semaine 9",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            }

            Divider(color = Color(0xFFE5D4C3), thickness = 0.8.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Âge gestationnel actuel estimé :",
                        fontSize = 11.sp,
                        color = MutedText
                    )
                    Text(
                        text = "$currentWeeks semaines et $currentDaysRemainder jours",
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = InkText
                    )
                    Text(
                        text = if (daysUntilGraduation > 0)
                            "Encore ~$daysUntilGraduation jours avant la graduation officielle chez l'obstétricien."
                        else
                            "Jalon de graduation atteint ! Félicitations pour la transition vers votre gynécologue obstétricien.",
                        fontSize = 10.5.sp,
                        color = GoldSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BorderLine)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$daysUntilGraduation",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldPrimary,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "JOURS CIBLE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedText
                        )
                    }
                }
            }

            // Progress bar to graduation
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = GoldPrimary,
                trackColor = Color(0xFFEADFD2)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Transfert FET", fontSize = 9.5.sp, color = MutedText)
                Text(text = "1ère Échographie (S6)", fontSize = 9.5.sp, color = MutedText)
                Text(text = "Graduation (S8-S10)", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
            }
        }
    }
}

data class SupportMedItem(
    val id: String,
    val icon: String,
    val name: String,
    val dosage: String,
    val scheduleTime: String,
    val instructions: String,
    var isTaken: Boolean = false
)

@Composable
fun ContinuedSupportMedsSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var meds by remember {
        mutableStateOf(
            listOf(
                SupportMedItem(
                    id = "med_pio",
                    icon = "💉",
                    name = "Progestérone en Huile (PIO)",
                    dosage = "50 mg / 1 mL",
                    scheduleTime = "08 h 00",
                    instructions = "Injection intramusculaire quotidienne. Réchauffer légèrement l'huile avant l'application pour réduire l'inconfort.",
                    isTaken = true
                ),
                SupportMedItem(
                    id = "med_suppositories",
                    icon = "💊",
                    name = "Ovules de Progestérone Vaginale",
                    dosage = "200 mg (3x par jour)",
                    scheduleTime = "08 h 00 · 14 h 00 · 22 h 00",
                    instructions = "Soutien progestatif endométrial continu prescrit par RMA NY pour maintenir l'épaisseur de l'endomètre.",
                    isTaken = false
                ),
                SupportMedItem(
                    id = "med_estrogen",
                    icon = "🌿",
                    name = "Œstrogènes (Estrace / Patchs)",
                    dosage = "2 mg",
                    scheduleTime = "09 h 00 · 20 h 00",
                    instructions = "Soutien hormonal combiné poursuivi jusqu'à la transition complète vers l'obstétricien.",
                    isTaken = false
                )
            )
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEAF4ED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💊", fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = "Médication de soutien continu",
                            fontSize = 14.5.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = InkText
                        )
                        Text(
                            text = "Progestérone (PIO / Ovules) & Œstrogènes post-transfert",
                            fontSize = 10.5.sp,
                            color = MutedText
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF6ECE0)
                ) {
                    Text(
                        text = "Essentiel",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }
            }

            Divider(color = BorderLine, thickness = 0.8.dp)

            Text(
                text = "Ces traitements hormonaux sont indispensables pendant le premier trimestre de grossesse pour soutenir l'implantation jusqu'au relais placentaire.",
                fontSize = 11.5.sp,
                color = MutedText,
                lineHeight = 16.sp
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                meds.forEach { med ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (med.isTaken) Color(0xFFF4F9F5) else Color(0xFFFAF7F3),
                        border = BorderStroke(1.dp, if (med.isTaken) Color(0xFFC7E2D0) else Color(0xFFE8DFC9))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                    Text(text = med.icon, fontSize = 18.sp)
                                    Column {
                                        Text(
                                            text = med.name,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = InkText
                                        )
                                        Text(
                                            text = "${med.dosage} · ⏰ ${med.scheduleTime}",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = GoldPrimary
                                        )
                                    }
                                }

                                Checkbox(
                                    checked = med.isTaken,
                                    onCheckedChange = { checked ->
                                        meds = meds.map { if (it.id == med.id) it.copy(isTaken = checked) else it }
                                        Toast.makeText(
                                            context,
                                            if (checked) "Prise enregistrée : ${med.name}" else "Marqué comme non pris",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = SuccessGreen,
                                        uncheckedColor = GoldPrimary
                                    )
                                )
                            }

                            Text(
                                text = med.instructions,
                                fontSize = 10.5.sp,
                                color = Color(0xFF6B584E),
                                lineHeight = 14.sp
                            )

                            // WorkManager reminder trigger button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        MedicationReminderScheduler.scheduleReminder(
                                            context = context,
                                            eventId = "support_med_${med.id}",
                                            title = "Rappel Soutien : ${med.name}",
                                            details = "⏰ Heure de prise : ${med.scheduleTime} — ${med.dosage}. ${med.instructions}",
                                            delaySeconds = 15L,
                                            timeLabel = med.scheduleTime
                                        )
                                        Toast.makeText(
                                            context,
                                            "Rappel WorkManager programmé pour ${med.name} !",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "🔔 Programmer alerte WorkManager",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
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
