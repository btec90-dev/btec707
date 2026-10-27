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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChecklistItem
import com.example.ui.theme.*

data class ProtocolStepItem(
    val stepNumber: Int,
    val icon: String,
    val title: String,
    val dateText: String,
    val timeText: String = "",
    val location: String = "",
    val daysRemaining: Int = 0,
    val description: String,
    val isMajor: Boolean = false,
    val isDone: Boolean = false
)

@Composable
fun HomeScreen(
    checklistItems: List<ChecklistItem>,
    onToggleCheck: (String) -> Unit,
    onNavigateTab: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedTimelineTab by remember { mutableStateOf("september") } // Default to September 2026 to see full September steps

    val septemberMilestones = listOf(
        ProtocolStepItem(
            stepNumber = 1,
            icon = "💊",
            title = "Suppression ovarienne (Nortrel)",
            dateText = "1 sept. 2026",
            timeText = "08 h 00",
            location = "Domicile",
            description = "Prise quotidienne de contraceptif oral pour bloquer l'axe hormonal et synchroniser les follicules antraux.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 2,
            icon = "📦",
            title = "Réception pharmacie de stimulation",
            dateText = "5 sept. 2026",
            timeText = "10 h 30",
            location = "Domicile (MDRX / Freedom Fertility)",
            description = "Vérification au froid : Gonal-F, Menopur, Cetrotide, Lupron, Ovidrel/Pregnyl, aiguilles et conteneur.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 3,
            icon = "📞",
            title = "Coordination infirmière RMA",
            dateText = "10 sept. 2026",
            timeText = "14 h 00",
            location = "RMA of New York (Téléconsultation)",
            description = "Revue détaillée du calendrier d'injections à heure fixe et validation préalable de l'assurance.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 4,
            icon = "✍️",
            title = "Consentements RMA & Biopsie PGT-A",
            dateText = "15 sept. 2026",
            timeText = "11 h 00",
            location = "Portail RMA Engage",
            description = "Signature électronique des consentements pour la ponction ovocytaire, l'ICSI et le test génétique PGT-A.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 5,
            icon = "🛑",
            title = "Dernier comprimé de Nortrel",
            dateText = "18 sept. 2026",
            timeText = "21 h 00",
            location = "Domicile",
            description = "Arrêt du blocage ovarien. Hydratation optimale recommandée avant l'échographie d'entrée en cycle.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 6,
            icon = "🔬",
            title = "Échographie basale & Bilan hormonal J1",
            dateText = "19 sept. 2026",
            timeText = "07 h 15",
            location = "RMA East Side (635 Madison Ave)",
            description = "CFA = 28 follicules antraux, endomètre fin (< 4 mm), E2 bas (< 50 pg/mL). Feu vert médical pour débuter.",
            isMajor = true,
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 7,
            icon = "🧪",
            title = "Session d'apprentissage & Préparation stylos",
            dateText = "20 sept. 2026",
            timeText = "19 h 30",
            location = "Domicile",
            description = "Réglage du stylo Gonal-F et reconstitution de la solution Menopur avec l'aiguille de mélange Q-Cap.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 8,
            icon = "💉",
            title = "Lancement de la stimulation ovarienne (J1)",
            dateText = "21 sept. 2026",
            timeText = "20 h 00 précises",
            location = "Domicile",
            description = "Premières injections sous-cutanées : Gonal-F 225 UI + Menopur 75 UI dans la paroi abdominale.",
            isMajor = true,
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 9,
            icon = "🏥",
            title = "Contrôle de stimulation n°1 (Monitoring #1)",
            dateText = "23 sept. 2026",
            timeText = "07 h 00 (PDS) & 20 h 00",
            location = "RMA of New York (East Side)",
            description = "Prise de sang : E2 = 480 pg/mL. Échographie : 18 follicules en développement (9-11 mm).",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 10,
            icon = "🛡️",
            title = "Contrôle n°2 & Ajout Cetrotide (Antagoniste)",
            dateText = "25 sept. 2026",
            timeText = "07 h 15 & 08 h 15",
            location = "RMA East Side & Domicile",
            description = "Follicules à 13-14 mm. 08 h 15 : Début Cetrotide 0.25 mg sous-cutané pour bloquer l'ovulation spontanée.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 11,
            icon = "📊",
            title = "Contrôle de stimulation n°3 (Monitoring #3)",
            dateText = "27 sept. 2026",
            timeText = "07 h 30 & 20 h 00",
            location = "RMA of New York (East Side)",
            description = "E2 = 1450 pg/mL. Échographie : 32 follicules visibles (14-17 mm), endomètre trilaminaire à 9.4 mm.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 12,
            icon = "📈",
            title = "Contrôle n°4 (Maturation avancée)",
            dateText = "28 sept. 2026",
            timeText = "07 h 15 & 20 h 00",
            location = "RMA of New York (East Side)",
            description = "Plus de 35 follicules en développement dont plus de 20 > 17 mm. E2 > 2400 pg/mL. Dernière injection de stimulation.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 13,
            icon = "⚡",
            title = "DÉCLENCHEMENT D'OVULATION (Trigger Shot)",
            dateText = "29 sept. 2026",
            timeText = "23 h 00 PRÉCISES",
            location = "Domicile (Sous-cutané)",
            description = "Double déclenchement Lupron 80 UI (4 mg) + hCG Pregnyl exactement 36h avant la ponction pour la maturation finale.",
            isMajor = true,
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 14,
            icon = "🩺",
            title = "Contrôle biologique post-déclenchement",
            dateText = "30 sept. 2026",
            timeText = "08 h 45 (PDS) & 19 h 00",
            location = "RMA East Side & Gramercy Center",
            description = "PDS confirmant la montée de LH/Progestérone (déclenchement réussi). 19 h 00 : Consignes de jeûne strict dès minuit.",
            isDone = true
        ),
        ProtocolStepItem(
            stepNumber = 15,
            icon = "🥚",
            title = "PONCTION OVOCYTAIRE (OPU) : 42 OVOCYTES !",
            dateText = "1 oct. 2026",
            timeText = "06 h 45 (Arrivée) · 08 h 00",
            location = "Gramercy Surgery Center / RMA",
            description = "Intervention sous sédation douce : récolte exceptionnelle de 42 ovocytes (34 matures MII, 11 fécondés par ICSI, 8 blastocystes vitrifiés).",
            isMajor = true,
            isDone = true
        )
    )

    val upcomingMilestones = listOf(
        ProtocolStepItem(
            stepNumber = 1,
            icon = "🧬",
            title = "Résultats PGT-A",
            dateText = "27 oct. 2026",
            timeText = "Dans la journée",
            location = "Portail RMA / Igenomix",
            daysRemaining = 2,
            description = "Analyse chromosomique des 8 blastocystes vitrifiés."
        ),
        ProtocolStepItem(
            stepNumber = 2,
            icon = "🏥",
            title = "Hystérosonographie SIS",
            dateText = "28 oct. 2026",
            timeText = "08 h 00",
            location = "RMA East Side (635 Madison)",
            daysRemaining = 3,
            description = "Examen de la cavité utérine avec sérum physiologique pour évaluer l'endomètre."
        ),
        ProtocolStepItem(
            stepNumber = 3,
            icon = "🔬",
            title = "Hystéroscopie opératoire",
            dateText = "30 oct. 2026",
            timeText = "07 h 30 (Arrivée)",
            location = "Gramercy Surgery Center",
            daysRemaining = 5,
            description = "Intervention ambulatoire pour préparer une cavité utérine optimale avant le transfert."
        ),
        ProtocolStepItem(
            stepNumber = 4,
            icon = "✨",
            title = "Transfert d'Embryon (FET)",
            dateText = "31 oct. 2026",
            timeText = "Date cible",
            location = "RMA of New York",
            daysRemaining = 6,
            description = "Le grand jour de l'implantation de votre blastocyste.",
            isMajor = true
        ),
        ProtocolStepItem(
            stepNumber = 5,
            icon = "🧪",
            title = "Prise de sang Bêta-hCG",
            dateText = "8 nov. 2026",
            timeText = "08 h 00",
            location = "RMA East Side",
            daysRemaining = 14,
            description = "Dosage sanguin officiel de l'hormone de grossesse (Bêta 1).",
            isMajor = true
        ),
        ProtocolStepItem(
            stepNumber = 6,
            icon = "📈",
            title = "Bêta-hCG n°2 (Évolution)",
            dateText = "10 nov. 2026",
            timeText = "08 h 00",
            location = "RMA East Side",
            daysRemaining = 16,
            description = "Vérification du doublage du taux d'hCG et de la vitalité précoce."
        ),
        ProtocolStepItem(
            stepNumber = 7,
            icon = "💓",
            title = "1ère Échographie (Cœur)",
            dateText = "22 nov. 2026",
            timeText = "09 h 30",
            location = "RMA East Side",
            daysRemaining = 28,
            description = "Premier son et battement de cœur de votre futur bébé !",
            isMajor = true
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Banner (mobile-optimized)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            BannerGradientStart,
                            Color(0xFFFCF9F5),
                            BannerGradientEnd
                        )
                    )
                )
                .border(1.dp, BorderLine, RoundedCornerShape(18.dp))
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "VOTRE PARCOURS FIV",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF5C3B24),
                    letterSpacing = 1.6.sp
                )
                Text(
                    text = "De la première injection\nau premier battement de cœur",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif,
                    color = InkText,
                    lineHeight = 26.sp
                )
                Text(
                    text = "Chaque étape de votre protocole chez RMA of New York est documentée avec dates, heures et détails cliniques.",
                    fontSize = 12.sp,
                    color = MutedText,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                val context = androidx.compose.ui.platform.LocalContext.`current`
                Button(
                    onClick = {
                        com.example.util.PdfHelper.generateAndSharePdf(context, checklistItems)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "📥", fontSize = 14.sp)
                        Text(
                            text = "Télécharger / Partager le Résumé PDF",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Cumulative Tracking & PGT Update with Canvas Donut Chart (Recharts equivalent)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF6EE)),
            border = BorderStroke(1.dp, Color(0xFFD8C7B3))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "🧬", fontSize = 18.sp)
                    Column {
                        Text(text = "Suivi Cumulatif Global (Résultats PGT-A)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5C3B24))
                        Text(text = "Ratio d'embryons sains (euploïdes) par rapport aux anormaux (RMA NY)", fontSize = 11.sp, color = MutedText)
                    }
                }
                Divider(color = Color(0xFFE5D4C3))
                EmbryoDonutChart(euploid = 5, abnormal = 3)
            }
        }

        // Protocol & Pregnancy Dynamic Days Counter Widget
        val startDate = java.time.LocalDate.of(2026, 9, 21)
        val today = java.time.LocalDate.now()
        val dynamicDaysElapsed = java.time.temporal.ChronoUnit.DAYS.between(startDate, today).coerceAtLeast(1)
        val dynamicDaysRemaining = (280 - dynamicDaysElapsed).coerceAtLeast(0)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF6F0)),
            border = BorderStroke(1.dp, GoldPrimary)
        ) {
            Row(
                modifier = Modifier.padding(14.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "⏳ Compteur Dynamique Protocole", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                    Text(text = "Début protocole : 21 sept. 2026 · Terme estimé : Juin 2027", fontSize = 10.5.sp, color = MutedText)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(shape = RoundedCornerShape(10.dp), color = Color.White, border = BorderStroke(1.dp, BorderLine)) {
                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$dynamicDaysElapsed", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                            Text(text = "JOURS ÉCOULÉS", fontSize = 8.sp, color = MutedText)
                        }
                    }
                    Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFEAF4ED), border = BorderStroke(1.dp, Color(0xFFBFE0CB))) {
                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$dynamicDaysRemaining", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            Text(text = "JOURS RESTANTS", fontSize = 8.sp, color = SuccessGreen)
                        }
                    }
                }
            }
        }

        // Graduation Countdown & Continued Support Meds
        GraduationCountdownCard()
        ContinuedSupportMedsSection()

        // Section Milestones with Tab Switching & Interactive Date Filter above Milestones
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            var selectedDateFilter by remember { mutableStateOf("Tous") }
            val dateFilters = listOf("Tous", "19 sept.", "21 sept.", "29 sept.", "1 oct.", "27 oct.", "31 oct.")

            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Header with Tab Switcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (selectedTimelineTab == "september") "♡ Les étapes de Septembre 2026" else "♡ En route vers la Grossesse",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = GoldPrimary
                        )
                        Text(
                            text = if (selectedTimelineTab == "september") "Stimulation, contrôles et ponction (15 étapes avec dates & heures)" else "Compte à rebours numérique des prochaines étapes",
                            fontSize = 11.sp,
                            color = MutedText
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (selectedTimelineTab == "september") Color(0xFFEBF4EE) else Color(0xFFFBF4E8),
                        border = BorderStroke(1.dp, if (selectedTimelineTab == "september") Color(0xFFCADECF) else Color(0xFFEAD4BC))
                    ) {
                        Text(
                            text = if (selectedTimelineTab == "september") "15 ÉTAPES ✓" else "7 ÉTAPES",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (selectedTimelineTab == "september") SuccessGreen else GoldPrimary
                        )
                    }
                }

                // Interactive Tab Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedTimelineTab = "september" },
                        color = if (selectedTimelineTab == "september") GoldPrimary else Color(0xFFF6F0E7),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Septembre (Stimulation & Ponction)",
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTimelineTab == "september") Color.White else Color(0xFF5C3B24),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedTimelineTab = "upcoming" },
                        color = if (selectedTimelineTab == "upcoming") GoldPrimary else Color(0xFFF6F0E7),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Octobre / Nov. (FET & Bêta-hCG)",
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTimelineTab == "upcoming") Color.White else Color(0xFF5C3B24),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                // Interactive Calendar Date Filter above Milestones List
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "📅 Filtrer les jalons par date :",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5C3B24)
                    )
                    androidx.compose.foundation.lazy.LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(dateFilters.size) { index ->
                            val filter = dateFilters[index]
                            val isSelected = selectedDateFilter == filter
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedDateFilter = filter },
                                color = if (isSelected) GoldPrimary else Color(0xFFF7EEE4),
                                border = BorderStroke(1.dp, if (isSelected) GoldPrimary else Color(0xFFEAD4BC)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = filter,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color(0xFF5C3B24)
                                )
                            }
                        }
                    }
                }

                Divider(color = BorderLine)

                // Render Filtered Milestones
                val activeList = if (selectedTimelineTab == "september") septemberMilestones else upcomingMilestones
                val filteredActiveList = if (selectedDateFilter == "Tous") {
                    activeList
                } else {
                    activeList.filter { it.dateText.contains(selectedDateFilter, ignoreCase = true) }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (filteredActiveList.isEmpty()) {
                        Text(
                            text = "Aucun jalon pour la date sélectionnée ($selectedDateFilter).",
                            fontSize = 11.5.sp,
                            color = MutedText,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        filteredActiveList.forEach { item ->
                            DetailedMilestoneRow(item = item)
                        }
                    }
                }
            }
        }

        // Care Team Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                    Text(
                        text = "✧ Votre équipe de soins",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF5C3B24)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CareItem("PATIENTE", "Fatou", Modifier.weight(1f))
                    CareItem("CLINIQUE", "RMA NY East", Modifier.weight(1.2f))
                    CareItem("LABO", "Alex (Embryo)", Modifier.weight(1f))
                }
            }
        }

        // Lab Stats Section
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "VOTRE DOSSIER EN BREF",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MutedText,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Les chiffres du laboratoire",
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                color = InkText
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("🥚", "42", "Ovocytes prélevés", Modifier.weight(1f))
                StatCard("◉", "34", "Ovocytes matures", Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("✧", "11", "Fécondés ICSI", Modifier.weight(1f))
                StatCard("❄", "8", "Blastocystes", Modifier.weight(1f))
            }
        }

        // Immediate Priorities Preview (Ultra-compact layout to fit perfectly)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✓ Vos prochaines actions",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5C3B24),
                        maxLines = 1
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFEBDCCF)
                    ) {
                        Text(
                            text = "OCTOBRE 2026",
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp),
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF543722)
                        )
                    }
                }

                Divider(color = BorderLine, thickness = 0.5.dp)

                checklistItems.take(3).forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleCheck(item.id) }
                            .padding(vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Checkbox(
                            checked = item.isCompleted,
                            onCheckedChange = { onToggleCheck(item.id) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = SuccessGreen,
                                uncheckedColor = GoldPrimary
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = InkText,
                                maxLines = 1
                            )
                            Text(
                                text = item.dateText,
                                fontSize = 8.5.sp,
                                color = MutedText,
                                maxLines = 1
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = { onNavigateTab("checklist") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 1.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = GoldPrimary
                    ),
                    border = BorderStroke(1.dp, GoldPrimary)
                ) {
                    Text(
                        text = "Voir toute la liste",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DetailedMilestoneRow(item: ProtocolStepItem) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (item.isMajor) Color(0xFFFBF6EE) else Color(0xFFFAF7F3),
        border = BorderStroke(1.dp, if (item.isMajor) Color(0xFFDFCFBE) else Color(0xFFEFE6DB))
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (item.isMajor) Color(0xFFF4EBD8) else Color(0xFFEFE5D8)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.icon, fontSize = 16.sp)
            }

            // Description, Date & Time
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = item.title,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkText,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "📅 ${item.dateText}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                    if (item.timeText.isNotEmpty()) {
                        Text(
                            text = "⏰ ${item.timeText}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B584E)
                        )
                    }
                }
                if (item.location.isNotEmpty()) {
                    Text(
                        text = "📍 ${item.location}",
                        fontSize = 9.5.sp,
                        color = Color(0xFF7A685D),
                        maxLines = 1
                    )
                }
                Text(
                    text = item.description,
                    fontSize = 10.5.sp,
                    color = MutedText,
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }

            // Badge or Countdown Box
            if (item.isDone) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFE8F4ED),
                    border = BorderStroke(1.dp, Color(0xFFBFE0CB)),
                    modifier = Modifier.widthIn(min = 52.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "✓",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SuccessGreen
                        )
                        Text(
                            text = "FAIT",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SuccessGreen,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (item.daysRemaining <= 6) Color(0xFFE8F4ED) else Color(0xFFF6ECE0),
                    border = BorderStroke(
                        1.dp,
                        if (item.daysRemaining <= 6) Color(0xFFBFE0CB) else Color(0xFFE2D1BE)
                    ),
                    modifier = Modifier.widthIn(min = 54.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${item.daysRemaining}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (item.daysRemaining <= 6) SuccessGreen else GoldPrimary,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = if (item.daysRemaining <= 1) "JOUR" else "JOURS",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MutedText,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CareItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFFFDFAF6), RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFEBDCCB), RoundedCornerShape(10.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = label, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = MutedText)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = InkText, maxLines = 1)
    }
}

@Composable
fun StatCard(icon: String, number: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = icon, fontSize = 20.sp)
            Text(
                text = number,
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary
            )
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = MutedText,
                maxLines = 1
            )
        }
    }
}

@Composable
fun EmbryoDonutChart(euploid: Int, abnormal: Int) {
    val total = euploid + abnormal
    val euploidAngle = if (total > 0) (euploid.toFloat() / total) * 360f else 0f
    val abnormalAngle = if (total > 0) (abnormal.toFloat() / total) * 360f else 0f

    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(76.dp)) {
            var startAngle = -90f
            drawArc(
                color = SuccessGreen,
                startAngle = startAngle,
                sweepAngle = euploidAngle,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 18f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
            startAngle += euploidAngle
            drawArc(
                color = Color(0xFFD97706),
                startAngle = startAngle,
                sweepAngle = abnormalAngle,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 18f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(text = "Rapport génétique PGT-A (8 Blastocystes)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5C3B24))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(SuccessGreen))
                Text(text = "Sains (Euploïdes) : $euploid (62.5%)", fontSize = 11.sp, color = InkText)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFD97706)))
                Text(text = "Anormaux : $abnormal (37.5%)", fontSize = 11.sp, color = InkText)
            }
        }
    }
}
