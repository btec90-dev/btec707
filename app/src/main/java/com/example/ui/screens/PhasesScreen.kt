package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class DetailedPhaseStep(
    val dateText: String,
    val timeText: String,
    val title: String,
    val location: String,
    val description: String,
    val isDone: Boolean = true
)

@Composable
fun PhasesScreen(
    sisCompleted: Boolean,
    onToggleSis: () -> Unit,
    hysteroCompleted: Boolean,
    onToggleHystero: () -> Unit
) {
    val scrollState = rememberScrollState()

    val septemberSteps = listOf(
        DetailedPhaseStep(
            dateText = "1 sept. 2026",
            timeText = "08 h 00",
            title = "Suppression ovarienne (Nortrel)",
            location = "Domicile",
            description = "Prise quotidienne de contraceptif oral pour bloquer l'activité ovarienne basale et synchroniser la cohorte folliculaire."
        ),
        DetailedPhaseStep(
            dateText = "5 sept. 2026",
            timeText = "10 h 30",
            title = "Réception pharmacie de stimulation",
            location = "Domicile (MDRX / Freedom Fertility)",
            description = "Réception et contrôle au froid : Gonal-F, Menopur, Cetrotide, Lupron, Ovidrel/Pregnyl et aiguilles."
        ),
        DetailedPhaseStep(
            dateText = "10 sept. 2026",
            timeText = "14 h 00",
            title = "Coordination infirmière RMA",
            location = "RMA of New York (Téléconsultation)",
            description = "Revue détaillée du protocole de stimulation, des horaires stricts d'injection et des autorisations d'assurance."
        ),
        DetailedPhaseStep(
            dateText = "15 sept. 2026",
            timeText = "11 h 00",
            title = "Consentements éclairés & PGT-A",
            location = "Portail RMA Engage",
            description = "Validation électronique des protocoles de ponction transvaginale, ICSI et biopsie du trophectoderme pour analyse génétique."
        ),
        DetailedPhaseStep(
            dateText = "18 sept. 2026",
            timeText = "21 h 00",
            title = "Dernier comprimé de Nortrel",
            location = "Domicile",
            description = "Arrêt de la mise au repos ovarien. Hydratation abondante avant le bilan d'entrée en cycle."
        ),
        DetailedPhaseStep(
            dateText = "19 sept. 2026",
            timeText = "07 h 15",
            title = "Échographie basale & Bilan hormonal (J1)",
            location = "RMA East Side (635 Madison Ave)",
            description = "Échographie pelvienne : endomètre fin (< 4 mm), CFA = 28 follicules antraux. Prise de sang : E2 < 50 pg/mL, Progestérone < 0.5 ng/mL. Feu vert médical pour la stimulation."
        ),
        DetailedPhaseStep(
            dateText = "20 sept. 2026",
            timeText = "19 h 30",
            title = "Préparation des stylos & Reconstitution",
            location = "Domicile",
            description = "Ajustement du stylo injecteur Gonal-F et mélange Menopur 75 UI avec le solvant stérile via aiguille Q-Cap."
        ),
        DetailedPhaseStep(
            dateText = "21 sept. 2026",
            timeText = "20 h 00",
            title = "Lancement stimulation ovarienne (Jour 1)",
            location = "Domicile",
            description = "20 h 00 précises : Premières injections sous-cutanées de Gonal-F 225 UI + Menopur 75 UI dans la zone abdominale."
        ),
        DetailedPhaseStep(
            dateText = "22 sept. 2026",
            timeText = "20 h 00",
            title = "Stimulation Jour 2",
            location = "Domicile",
            description = "Injections vespérales : Gonal-F 225 UI + Menopur 75 UI. Bonne tolérance, repos."
        ),
        DetailedPhaseStep(
            dateText = "23 sept. 2026",
            timeText = "07 h 00 & 20 h 00",
            title = "Contrôle de stimulation n°1 (Monitoring #1)",
            location = "RMA of New York (East Side)",
            description = "Prise de sang à 07 h 00 : Estradiol (E2) = 480 pg/mL, Progestérone = 0.3 ng/mL. Échographie : 18 follicules émergents (9-11 mm). 20 h 00 : Gonal-F 225 UI + Menopur 75 UI."
        ),
        DetailedPhaseStep(
            dateText = "24 sept. 2026",
            timeText = "20 h 00",
            title = "Stimulation Jour 4",
            location = "Domicile",
            description = "Injections à heure fixe : Gonal-F 225 UI + Menopur 75 UI. Hydratation accrue (électrolytes)."
        ),
        DetailedPhaseStep(
            dateText = "25 sept. 2026",
            timeText = "07 h 15 & 08 h 15",
            title = "Contrôle n°2 & Début Antagoniste (Cetrotide)",
            location = "RMA of New York (East Side)",
            description = "Échographie : follicules à 13-14 mm, endomètre à 8.1 mm. 08 h 15 : Début Cetrotide 0.25 mg sous-cutané pour bloquer l'ovulation spontanée. 20 h 00 : Gonal-F + Menopur."
        ),
        DetailedPhaseStep(
            dateText = "26 sept. 2026",
            timeText = "08 h 00 & 20 h 00",
            title = "Stimulation Jour 6 (Triple médication)",
            location = "Domicile",
            description = "08 h 00 : Cetrotide 0.25 mg. 20 h 00 : Gonal-F 225 UI + Menopur 75 UI. Pression pelvienne normale due à l'activité ovarienne."
        ),
        DetailedPhaseStep(
            dateText = "27 sept. 2026",
            timeText = "07 h 30 & 20 h 00",
            title = "Contrôle de stimulation n°3 (Monitoring #3)",
            location = "RMA of New York (East Side)",
            description = "Bilan sanguin : E2 = 1450 pg/mL, LH bloquée. Échographie : 32 follicules visibles (14-17 mm), endomètre trilaminaire à 9.4 mm. Continuation du traitement."
        ),
        DetailedPhaseStep(
            dateText = "28 sept. 2026",
            timeText = "07 h 15 & 20 h 00",
            title = "Contrôle n°4 (Maturation avancée)",
            location = "RMA of New York (East Side)",
            description = "Échographie : plus de 35 follicules en développement dont plus de 20 supérieurs à 17 mm. E2 > 2400 pg/mL. 20 h 00 : Dernière injection de stimulation."
        ),
        DetailedPhaseStep(
            dateText = "29 sept. 2026",
            timeText = "07 h 00 & 23 h 00 PRÉCISES",
            title = "Contrôle final & DÉCLENCHEMENT (Trigger Shot)",
            location = "RMA East Side & Domicile",
            description = "07 h 00 : Confirmation de maturité ovocytaire. 23 h 00 PILE (exactement 36h avant la ponction) : Double déclenchement (Lupron 80 UI / 4 mg + mini hCG Pregnyl) pour détacher les ovocytes."
        ),
        DetailedPhaseStep(
            dateText = "30 sept. 2026",
            timeText = "08 h 45 & 19 h 00",
            title = "Contrôle post-déclenchement & Consignes chirurgicales",
            location = "RMA East Side & Gramercy Center",
            description = "08 h 45 : Bilan sanguin vérifiant la montée de LH et de progestérone (déclenchement réussi). 19 h 00 : Appel de Gramercy Surgery Center : jeûne strict dès minuit. Arrivée fixée à 06 h 45 le lendemain."
        ),
        DetailedPhaseStep(
            dateText = "1 oct. 2026",
            timeText = "06 h 45 (Arrivée) · 08 h 00 (OPU)",
            title = "PONCTION OVOCYTAIRE (OPU) : 42 Ovocytes recueillis !",
            location = "Gramercy Surgery Center / RMA NY",
            description = "Ponction sous sédation veineuse : 42 ovocytes prélevés ! 34 matures et 11 fécondés par ICSI, menant à 8 blastocystes vitrifiés de qualité supérieure."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "VUE D’ENSEMBLE",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MutedText,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Les 4 phases de votre parcours",
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                color = InkText
            )
            Text(
                text = "Un chemin balisé avec amour et rigueur clinique.",
                fontSize = 12.sp,
                color = MutedText
            )
        }

        // 4 Phases Cards
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PhaseCard(
                num = "1",
                icon = "💉",
                title = "Stimulation et ponction",
                dates = "1 sept. – 1 oct. 2026",
                desc = "17 étapes détaillées : stimulation, contrôles quotidiens et ponction réussie de 42 ovocytes.",
                status = "✓ Terminée (17/17)",
                statusType = "done"
            )
            PhaseCard(
                num = "2",
                icon = "🧫",
                title = "Embryologie, Hystéroscopie & FET",
                dates = "2 oct. – début nov. 2026",
                desc = "8 blastocystes vitrifiés, examen SIS, chirurgie Gramercy et FET.",
                status = "● En cours",
                statusType = "active"
            )
            PhaseCard(
                num = "3",
                icon = "♡",
                title = "Suivi de début de grossesse",
                dates = "Après le transfert",
                desc = "Dosages bêta-hCG et premières échographies précoces chez RMA.",
                status = "À venir",
                statusType = "future"
            )
            PhaseCard(
                num = "4",
                icon = "♧",
                title = "Étapes obstétricales",
                dates = "Semaines 6–41",
                desc = "Dépistages, morphologie fœtale et accueil de votre bébé.",
                status = "À venir",
                statusType = "future"
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Timelines Section Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "HISTORIQUE ET ÉTAPES CLINIQUES",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MutedText,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Chronologie médicale détaillée",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                color = InkText
            )
        }

        // Timeline Phase 1 (Complete September Steps)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE8F2E9))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PHASE 1 · STIMULATION & PONCTION (SEPTEMBRE 2026)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2C6D4F)
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFD4EBD9)
                        ) {
                            Text(
                                text = "17 ÉTAPES",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SuccessGreen
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    septemberSteps.forEach { step ->
                        DetailedStepCard(step = step)
                    }
                }
            }
        }

        // Timeline Phase 2
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEEE1D3))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "PHASE 2 · EMBRYOLOGIE & FET (EN COURS)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF543722)
                    )
                }
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    TimelineRow("2 oct. · 10 h", "ICSI : 34 matures, 11 fécondés", "✓ Fait")
                    TimelineRow("9 oct. · 14 h", "8 blastocystes vitrifiés", "✓ Fait")
                    TimelineRow("13 oct. · 15 h", "Plan de décongélation soumis", "✓ Fait")
                    TimelineInteractiveRow(
                        date = "28 oct. · 08 h",
                        title = "Hystérosonographie SIS (RMA East)",
                        isDone = sisCompleted,
                        onToggle = onToggleSis
                    )
                    TimelineInteractiveRow(
                        date = "30 oct. · 07 h 30",
                        title = "Hystéroscopie opératoire (Gramercy)",
                        isDone = hysteroCompleted,
                        onToggle = onToggleHystero
                    )
                    TimelineRow("31 oct. 2026", "Transfert embryonnaire (FET envisagé)", "⏳ Date cible")
                }
            }
        }
    }
}

@Composable
fun DetailedStepCard(step: DetailedPhaseStep) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFAF8F5),
        border = BorderStroke(1.dp, Color(0xFFEBE0D2))
    ) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = GoldPrimary
                    ) {
                        Text(
                            text = step.dateText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "⏰ ${step.timeText}",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B584E)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE4F2E7)
                ) {
                    Text(
                        text = "✓ Réalisé",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            }

            Text(
                text = step.title,
                fontSize = 13.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = InkText
            )

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "📍", fontSize = 10.sp)
                Text(
                    text = step.location,
                    fontSize = 10.5.sp,
                    color = Color(0xFF7A685D)
                )
            }

            Text(
                text = step.description,
                fontSize = 11.sp,
                color = Color(0xFF4A403A),
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun PhaseCard(num: String, icon: String, title: String, dates: String, desc: String, status: String, statusType: String) {
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
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = num, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Text(text = icon, fontSize = 22.sp)
            }
            Text(text = title, fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = InkText)
            Text(text = "$dates\n$desc", fontSize = 11.5.sp, color = MutedText, lineHeight = 16.sp)

            val (bg, fg) = when (statusType) {
                "done" -> Color(0xFFE3F1E8) to Color(0xFF287454)
                "active" -> Color(0xFFF4E9DD) to Color(0xFF5C3B24)
                else -> Color(0xFFEDF0F2) to Color(0xFF67747C)
            }
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = bg
            ) {
                Text(
                    text = status,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = fg
                )
            }
        }
    }
}

@Composable
fun TimelineRow(date: String, text: String, badge: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = date, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldPrimary, modifier = Modifier.width(90.dp))
        Text(text = text, fontSize = 11.sp, color = Color(0xFF49545A), modifier = Modifier.weight(1f))
        Surface(
            shape = RoundedCornerShape(99.dp),
            color = Color(0xFFE9F3EC)
        ) {
            Text(text = badge, modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp), fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F644B))
        }
    }
}

@Composable
fun TimelineInteractiveRow(date: String, title: String, isDone: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDone) Color(0xFFEEF7F1) else Color.Transparent, RoundedCornerShape(6.dp))
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = date, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldPrimary, modifier = Modifier.width(90.dp))
        Text(text = title, fontSize = 11.sp, color = Color(0xFF49545A), modifier = Modifier.weight(1f))
        OutlinedButton(
            onClick = onToggle,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            modifier = Modifier.height(28.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isDone) Color(0xFFE3F1E8) else Color(0xFFFFF8EE),
                contentColor = if (isDone) Color(0xFF287454) else Color(0xFF8E6B29)
            )
        ) {
            Text(text = if (isDone) "✓ Complété" else "○ À faire", fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
        }
    }
}
