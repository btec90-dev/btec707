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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.util.MedicationReminderScheduler

data class CalendarEventDetail(
    val day: Int,
    val month: Int,
    val time: String,
    val title: String,
    val location: String,
    val details: String,
    val category: String = "Médical",
    val isCompleted: Boolean = true
)

@Composable
fun CalendarScreen(
    sisCompleted: Boolean,
    onToggleSis: () -> Unit,
    hysteroCompleted: Boolean,
    onToggleHystero: () -> Unit
) {
    val context = LocalContext.current
    var eventForReminder by remember { mutableStateOf<CalendarEventDetail?>(null) }
    var remindersRefreshKey by remember { mutableStateOf(0) }

    // Month navigation state: 9 = Sept, 10 = Oct, 11 = Nov
    var currentMonth by remember { mutableStateOf(9) } // Default to September 2026 as requested by user
    var selectedDay by remember { mutableStateOf(21) }

    val monthNames = mapOf(
        9 to "Septembre 2026",
        10 to "Octobre 2026",
        11 to "Novembre 2026"
    )

    // Complete detailed events for Fatou's protocol with exact date, time, location and medical details
    val detailedEvents = remember(sisCompleted, hysteroCompleted) {
        listOf(
            // SEPTEMBRE 2026 (Stimulation & Ponction)
            CalendarEventDetail(
                day = 1,
                month = 9,
                time = "08 h 00",
                title = "Suppression ovarienne & Priming (Nortrel)",
                location = "Domicile",
                category = "Médication",
                details = "Début de la prise quotidienne de Nortrel (contraceptif oestro-progestatif) pour mettre les ovaires au repos et synchroniser la cohorte des follicules antraux avant la stimulation."
            ),
            CalendarEventDetail(
                day = 5,
                month = 9,
                time = "10 h 30",
                title = "Réception pharmacie de stimulation",
                location = "Domicile (Freedom Fertility / MDRX)",
                category = "Logistique",
                details = "Réception et vérification de la chaîne du froid : stylos Gonal-F (Follitropine alfa), Menopur 75 UI, Cetrotide 0.25 mg, Lupron 4 mg, Ovidrel/Pregnyl, aiguilles 27G et conteneur pour déchets tranchants."
            ),
            CalendarEventDetail(
                day = 10,
                month = 9,
                time = "14 h 00",
                title = "Consultation de coordination infirmière",
                location = "RMA of New York (Téléconsultation)",
                category = "Consultation",
                details = "Revue détaillée du protocole d'antagoniste GnRH, vérification des règles de conservation au réfrigérateur (2°C-8°C) et validation préalable de la couverture d'assurance."
            ),
            CalendarEventDetail(
                day = 15,
                month = 9,
                time = "11 h 00",
                title = "Consentements RMA & Biopsie PGT-A",
                location = "Portail patient RMA Engage",
                category = "Administratif",
                details = "Validation numérique des consentements d'anesthésie pour la ponction transvaginale, de fécondation par ICSI et de biopsie du trophectoderme pour analyse génétique PGT-A."
            ),
            CalendarEventDetail(
                day = 18,
                month = 9,
                time = "21 h 00",
                title = "Dernier comprimé de Nortrel",
                location = "Domicile",
                category = "Médication",
                details = "Fin de la phase de suppression ovarienne. Hydratation abondante (2L d'eau/électrolytes par jour conseillés) avant l'échographie d'entrée en cycle le lendemain."
            ),
            CalendarEventDetail(
                day = 19,
                month = 9,
                time = "07 h 15",
                title = "Échographie basale & Bilan hormonal (J1)",
                location = "RMA of New York (East Side, 635 Madison Ave)",
                category = "Échographie & PDS",
                details = "Échographie endovaginale : endomètre fin (< 4 mm), CFA (comptage des follicules antraux) = 28 follicules. Prise de sang : E2 < 50 pg/mL, Progestérone < 0.5 ng/mL. Feu vert médical pour la stimulation."
            ),
            CalendarEventDetail(
                day = 20,
                month = 9,
                time = "19 h 30",
                title = "Session d'injection & Préparation",
                location = "Domicile",
                category = "Préparation",
                details = "Réglage du stylo Gonal-F, reconstitution du Menopur avec solution stérile et aiguille Q-Cap. Vérification de la technique d'injection sous-cutanée dans la paroi abdominale."
            ),
            CalendarEventDetail(
                day = 21,
                month = 9,
                time = "20 h 00",
                title = "Lancement de la stimulation ovarienne (Jour 1)",
                location = "Domicile",
                category = "Stimulation",
                details = "20 h 00 précises : Premières injections sous-cutanées de Gonal-F 225 UI + Menopur 75 UI dans la zone péri-ombilicale. Début de la croissance folliculaire contrôlée."
            ),
            CalendarEventDetail(
                day = 22,
                month = 9,
                time = "20 h 00",
                title = "Stimulation Jour 2",
                location = "Domicile",
                category = "Stimulation",
                details = "Injections sous-cutanées régulières : Gonal-F 225 UI + Menopur 75 UI. Alternance du côté gauche et droit de l'ombilic. Excellente tolérance."
            ),
            CalendarEventDetail(
                day = 23,
                month = 9,
                time = "07 h 00 & 20 h 00",
                title = "Contrôle de stimulation n°1 (Monitoring #1)",
                location = "RMA of New York (East Side)",
                category = "Monitoring",
                details = "Prise de sang à 07 h 00 : Estradiol (E2) = 480 pg/mL (forte réactivité), Progestérone = 0.3 ng/mL. Échographie : 18 follicules émergents (9-11 mm). Poursuite des doses à 20 h 00 (Gonal-F 225 UI + Menopur 75 UI)."
            ),
            CalendarEventDetail(
                day = 24,
                month = 9,
                time = "20 h 00",
                title = "Stimulation Jour 4",
                location = "Domicile",
                category = "Stimulation",
                details = "Injections de Gonal-F 225 UI + Menopur 75 UI. Hydratation renforcée en électrolytes (eau de coco, bouillon salé)."
            ),
            CalendarEventDetail(
                day = 25,
                month = 9,
                time = "07 h 15 & 08 h 15",
                title = "Contrôle n°2 & Ajout Cetrotide (Antagoniste)",
                location = "RMA of New York (East Side)",
                category = "Monitoring & Antagoniste",
                details = "Échographie : follicules dominants à 13-14 mm, endomètre trilaminaire à 8.1 mm. 08 h 15 : Première injection de Cetrotide 0.25 mg pour bloquer le pic ovulatoire spontané. 20 h 00 : Gonal-F + Menopur."
            ),
            CalendarEventDetail(
                day = 26,
                month = 9,
                time = "08 h 00 & 20 h 00",
                title = "Stimulation Jour 6 (Triple médication)",
                location = "Domicile",
                category = "Stimulation",
                details = "08 h 00 : Cetrotide 0.25 mg. 20 h 00 : Gonal-F 225 UI + Menopur 75 UI. Sensation normale de lourdeur ovarienne liée au volume folliculaire."
            ),
            CalendarEventDetail(
                day = 27,
                month = 9,
                time = "07 h 30 & 20 h 00",
                title = "Contrôle de stimulation n°3 (Monitoring #3)",
                location = "RMA of New York (East Side)",
                category = "Monitoring",
                details = "Prise de sang à 07 h 30 : Estradiol (E2) = 1450 pg/mL, LH bien freinée. Échographie : 32 follicules visibles (14-17 mm), endomètre à 9.4 mm. Continuation Cetrotide le matin et stimulation le soir."
            ),
            CalendarEventDetail(
                day = 28,
                month = 9,
                time = "07 h 15 & 20 h 00",
                title = "Contrôle de stimulation n°4 (Pré-maturité)",
                location = "RMA of New York (East Side)",
                category = "Monitoring",
                details = "Échographie : plus de 35 follicules en développement dont plus de 20 > 17 mm. E2 > 2400 pg/mL. Protocole préventif contre l'hyperstimulation : prescription d'un double déclenchement Lupron + mini-hCG. 20 h 00 : Dernière injection de stimulation."
            ),
            CalendarEventDetail(
                day = 29,
                month = 9,
                time = "07 h 00 & 23 h 00 PRÉCISES",
                title = "Contrôle final & DÉCLENCHEMENT (Trigger Shot)",
                location = "RMA East Side & Domicile",
                category = "Déclenchement",
                details = "07 h 00 : Échographie finale et confirmation de maturité ovocytaire. 23 h 00 PILE (exactement 36h avant ponction) : Double déclenchement (Lupron 80 UI / 4 mg + mini-dose hCG Pregnyl) pour induire la maturation finale."
            ),
            CalendarEventDetail(
                day = 30,
                month = 9,
                time = "08 h 45 & 19 h 00",
                title = "Contrôle post-déclenchement & Consignes ponction",
                location = "RMA of New York & Gramercy Center",
                category = "Pré-opératoire",
                details = "08 h 45 : Prise de sang de contrôle confirmant la montée de LH et progestérone (déclenchement réussi). 19 h 00 : Appel de Gramercy Surgery Center : jeûne strict dès minuit (ni eau ni nourriture). Arrivée fixée à 06 h 45 le lendemain."
            ),

            // OCTOBRE 2026 (Ponction & Préparation FET)
            CalendarEventDetail(
                day = 1,
                month = 10,
                time = "06 h 45 (Arrivée) · 08 h 00 (Procédure)",
                title = "PONCTION OVOCYTAIRE (OPU) : 42 Ovocytes !",
                location = "Gramercy Surgery Center / RMA of New York",
                category = "Chirurgie",
                details = "Intervention transvaginale sous sédation veineuse : récolte de 42 ovocytes ! Retour à domicile avec repos absolu et hydratation riche en sodium/électrolytes."
            ),
            CalendarEventDetail(
                day = 2,
                month = 10,
                time = "10 h 00",
                title = "Rapport de fécondation ICSI",
                location = "Laboratoire d'embryologie RMA NY",
                category = "Laboratoire",
                details = "Sur les 42 ovocytes : 34 étaient matures (MII) et 11 ont été fécondés avec succès par injection intracytoplasmique (ICSI). Début de culture en incubateur time-lapse."
            ),
            CalendarEventDetail(
                day = 6,
                month = 10,
                time = "11 h 30",
                title = "Début du développement des blastocystes",
                location = "Laboratoire d'embryologie RMA NY",
                category = "Laboratoire",
                details = "Suivi de la blastulation (J5-J6). Les embryons atteignent les stades morula et blastocyste expansé. Préparation de la biopsie du trophectoderme."
            ),
            CalendarEventDetail(
                day = 9,
                month = 10,
                time = "14 h 00",
                title = "Rapport d'embryologie : 8 blastocystes vitrifiés",
                location = "Laboratoire d'embryologie RMA NY",
                category = "Laboratoire",
                details = "8 magnifiques blastocystes de haute qualité ont été biopsiés pour analyse génétique PGT-A puis vitrifiés (cryoconservation ultra-rapide) en toute sécurité."
            ),
            CalendarEventDetail(
                day = 11,
                month = 10,
                time = "09 h 15",
                title = "Consultation de base post-ponction & Plan FET",
                location = "RMA of New York (East Side)",
                category = "Consultation",
                details = "Échographie pelvienne de contrôle pour vérifier la régression ovarienne post-ponction. Planification des examens utérins de préparation au transfert d'embryon congelé."
            ),
            CalendarEventDetail(
                day = 13,
                month = 10,
                time = "15 h 00",
                title = "Plan de décongélation soumis",
                location = "Portail patient RMA Engage",
                category = "Administratif",
                details = "Soumission officielle du formulaire de décongélation embryonnaire avec indication de la préférence pour un garçon (« Male »), sous réserve des résultats euploïdes PGT-A."
            ),
            CalendarEventDetail(
                day = 26,
                month = 10,
                time = "10 h 00",
                title = "Résultats PGT-A : 5 Embryons Normaux (Euploïdes) !",
                location = "Portail patient RMA / Igenomix",
                category = "Génétique",
                details = "Magnifique nouvelle ! Sur les 8 blastocystes testés, 5 sont revenus génétiquement normaux (euploïdes) et parfaits pour le transfert (FET)."
            ),
            CalendarEventDetail(
                day = 28,
                month = 10,
                time = "08 h 00",
                title = "Hystérosonographie par perfusion saline (SIS)",
                location = "RMA of New York (East Side, 635 Madison Ave)",
                category = "Examen utérin",
                details = "Examen de la cavité utérine avec instillation de sérum physiologique stérile sous contrôle échographique pour évaluer l'endomètre.",
                isCompleted = sisCompleted
            ),
            CalendarEventDetail(
                day = 30,
                month = 10,
                time = "07 h 30 (Arrivée) · Intervention programmée",
                title = "Hystéroscopie opératoire (Débridement)",
                location = "Gramercy Surgery Center",
                category = "Chirurgie",
                details = "Intervention ambulatoire mini-invasive pour inspecter et optimiser la cavité utérine (retrait de petits débris/adhérences) afin de maximiser le taux d'implantation embryonnaire.",
                isCompleted = hysteroCompleted
            ),
            CalendarEventDetail(
                day = 31,
                month = 10,
                time = "Date cible prévisionnelle",
                title = "Transfert d'Embryon Congelé (FET)",
                location = "RMA of New York",
                category = "Transfert",
                details = "Date cible pour le transfert du blastocyste choisi, sous réserve de la cicatrisation post-opératoire et de la validation médicale."
            ),

            // NOVEMBRE 2026 (Résultats & Suivi précoce)
            CalendarEventDetail(
                day = 1,
                month = 11,
                time = "Date alternative",
                title = "Transfert d'Embryon Congelé (Fenêtre alternative)",
                location = "RMA of New York",
                category = "Transfert",
                details = "Fenêtre alternative selon les recommandations de l'équipe clinique RMA après l'intervention chirurgicale à Gramercy."
            ),
            CalendarEventDetail(
                day = 8,
                month = 11,
                time = "08 h 00",
                title = "Prise de sang Bêta-hCG (Bêta n°1)",
                location = "RMA of New York (East Side)",
                category = "Test Grossesse",
                details = "Dosage quantitatif de l'hormone chorionique gonadotrope (hCG) dans le sang pour confirmer l'implantation et la grossesse."
            ),
            CalendarEventDetail(
                day = 10,
                month = 11,
                time = "08 h 00",
                title = "Bêta-hCG n°2 (Évolution & Doublage)",
                location = "RMA of New York (East Side)",
                category = "Test Grossesse",
                details = "Deuxième dosage sanguin 48h après la première prise pour vérifier que le taux d'hCG double correctement."
            ),
            CalendarEventDetail(
                day = 22,
                month = 11,
                time = "09 h 30",
                title = "1ère Échographie obstétricale (Activité cardiaque)",
                location = "RMA of New York (East Side)",
                category = "Échographie précoce",
                details = "Échographie pelvienne à 6-7 semaines d'aménorrhée : visualisation du sac gestationnel, de la vésicule vitelline et du premier battement de cœur fœtal."
            )
        )
    }

    // Days count and start offset for each month in 2026
    // Sept 2026: starts Tuesday (offset 1), 30 days
    // Oct 2026: starts Thursday (offset 3), 31 days
    // Nov 2026: starts Sunday (offset 6), 30 days
    val monthConfig = mapOf(
        9 to Pair(1, 30),
        10 to Pair(3, 31),
        11 to Pair(6, 30)
    )

    val (startOffset, totalDays) = monthConfig[currentMonth] ?: Pair(0, 31)
    val eventsThisMonth = detailedEvents.filter { it.month == currentMonth }
    val eventsByDay = eventsThisMonth.associateBy { it.day }
    val selectedEvent = eventsByDay[selectedDay]
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (eventForReminder != null) {
            ScheduleReminderModal(
                event = eventForReminder!!,
                onDismiss = { eventForReminder = null },
                onReminderScheduled = { remindersRefreshKey++ }
            )
        }

        // Screen Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "PLANNING MÉDICAL — PROTOCOLE FIV",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MutedText,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Calendrier de Fatou",
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                color = InkText
            )
            Text(
                text = "Toutes les étapes avec dates, heures exactes et détails cliniques complets.",
                fontSize = 12.sp,
                color = MutedText
            )
        }

        // Quick Month Selector Tabs (Septembre, Octobre, Novembre)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                9 to "Septembre (Stimulation)",
                10 to "Octobre (FET)",
                11 to "Novembre (Bêta-hCG)"
            ).forEach { (mNum, mLabel) ->
                val isCurrent = currentMonth == mNum
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            currentMonth = mNum
                            selectedDay = when (mNum) {
                                9 -> 21
                                10 -> 28
                                else -> 8
                            }
                        },
                    color = if (isCurrent) GoldPrimary else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, if (isCurrent) GoldPrimary else BorderLine),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (mNum) {
                                9 -> "Septembre"
                                10 -> "Octobre"
                                else -> "Novembre"
                            },
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) Color.White else InkText
                        )
                        Text(
                            text = when (mNum) {
                                9 -> "17 étapes"
                                10 -> "10 étapes"
                                else -> "4 étapes"
                            },
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isCurrent) Color.White.copy(alpha = 0.85f) else MutedText
                        )
                    }
                }
            }
        }

        // Calendar Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Month Header with Prev/Next buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = monthNames[currentMonth] ?: "",
                            fontSize = 19.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Text(
                            text = when (currentMonth) {
                                9 -> "Cycle de stimulation et ponction (Réalisé ✓)"
                                10 -> "Préparation utérine, SIS & FET (En cours)"
                                else -> "Attente des résultats et confirmation grossesse"
                            },
                            fontSize = 11.sp,
                            color = MutedText
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(
                            onClick = {
                                if (currentMonth > 9) {
                                    currentMonth--
                                    selectedDay = if (currentMonth == 9) 21 else 28
                                }
                            },
                            enabled = currentMonth > 9,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("‹", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = {
                                if (currentMonth < 11) {
                                    currentMonth++
                                    selectedDay = if (currentMonth == 10) 28 else 8
                                }
                            },
                            enabled = currentMonth < 11,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("›", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Divider(color = BorderLine)

                // Days of week header
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim").forEach { dayName ->
                        Text(
                            text = dayName,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(36.dp)
                        )
                    }
                }

                // Days grid
                val totalCells = startOffset + totalDays
                val rows = (totalCells + 6) / 7

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (row in 0 until rows) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            for (col in 0 until 7) {
                                val cellIndex = row * 7 + col
                                val dayNum = cellIndex - startOffset + 1
                                if (dayNum in 1..totalDays) {
                                    val event = eventsByDay[dayNum]
                                    val hasEvent = event != null
                                    val isSelected = (dayNum == selectedDay)
                                    val isDone = event?.isCompleted ?: false

                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                when {
                                                    isSelected -> GoldPrimary
                                                    hasEvent && isDone -> Color(0xFFEAF4ED)
                                                    hasEvent -> Color(0xFFF7EEE4)
                                                    else -> Color.Transparent
                                                }
                                            )
                                            .border(
                                                width = if (hasEvent && !isSelected) 1.dp else 0.dp,
                                                color = when {
                                                    isDone -> Color(0xFFD4E7D9)
                                                    hasEvent -> Color(0xFFE5D4C3)
                                                    else -> Color.Transparent
                                                },
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable(enabled = hasEvent) {
                                                selectedDay = dayNum
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = dayNum.toString(),
                                                fontSize = 11.5.sp,
                                                fontWeight = if (hasEvent || isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color.White else InkText
                                            )
                                            if (hasEvent && !isSelected) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isDone) SuccessGreen else GoldPrimary)
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(38.dp))
                                }
                            }
                        }
                    }
                }

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendItem("Étape du protocole", GoldPrimary)
                    LegendItem("Validée / Réalisée", SuccessGreen)
                    LegendItem("Sélectionnée", GoldPrimary)
                }

                // Selected Event Detailed Card
                if (selectedEvent != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFCF9F5), RoundedCornerShape(12.dp))
                            .border(1.dp, BorderLine, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Date & Time header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (selectedEvent.isCompleted) Color(0xFFEAF4ED) else Color(0xFFF7EEE4),
                                    border = BorderStroke(1.dp, if (selectedEvent.isCompleted) Color(0xFFC7E2D0) else Color(0xFFE5D4C3))
                                ) {
                                    Text(
                                        text = "${selectedEvent.day} ${monthNames[selectedEvent.month]} · ${selectedEvent.time}",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedEvent.isCompleted) SuccessGreen else Color(0xFF8A532B)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFFF3EAD9)
                                ) {
                                    Text(
                                        text = selectedEvent.category,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = GoldPrimary
                                    )
                                }
                            }

                            Text(
                                text = selectedEvent.title,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = InkText
                            )

                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = "📍", fontSize = 11.sp)
                                Text(
                                    text = selectedEvent.location,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF6B584E)
                                )
                            }

                            Divider(color = Color(0xFFEDE0D4), thickness = 0.8.dp)

                            Text(
                                text = selectedEvent.details,
                                fontSize = 12.sp,
                                color = InkText,
                                lineHeight = 17.sp
                            )

                            // Interactive toggles for current upcoming procedures
                            if (currentMonth == 10 && selectedDay == 28) {
                                Button(
                                    onClick = onToggleSis,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (sisCompleted) Color(0xFFE8F4ED) else Color.White,
                                        contentColor = if (sisCompleted) SuccessGreen else GoldPrimary
                                    ),
                                    border = BorderStroke(1.dp, if (sisCompleted) SuccessGreen else GoldPrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (sisCompleted) "Marquer comme à faire ↺" else "Marquer comme réalisé ✓",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else if (currentMonth == 10 && selectedDay == 30) {
                                Button(
                                    onClick = onToggleHystero,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (hysteroCompleted) Color(0xFFE8F4ED) else Color.White,
                                        contentColor = if (hysteroCompleted) SuccessGreen else GoldPrimary
                                    ),
                                    border = BorderStroke(1.dp, if (hysteroCompleted) SuccessGreen else GoldPrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (hysteroCompleted) "Marquer comme à faire ↺" else "Marquer comme réalisé ✓",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else if (selectedEvent.isCompleted) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                    Text("Étape réalisée et consignée au dossier médical", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Medium)
                                }
                            }

                            val eventId = "evt_${selectedEvent.month}_${selectedEvent.day}_${selectedEvent.time.filter { it.isDigit() }}"
                            val isEventScheduled = remember(selectedEvent, remindersRefreshKey) {
                                MedicationReminderScheduler.isReminderScheduled(context, eventId)
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isEventScheduled) {
                                            MedicationReminderScheduler.cancelReminder(context, eventId)
                                            Toast.makeText(context, "Rappel annulé", Toast.LENGTH_SHORT).show()
                                            remindersRefreshKey++
                                        } else {
                                            eventForReminder = selectedEvent
                                        }
                                    },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isEventScheduled) Color(0xFFFBF4EB) else Color(0xFFFAF7F2),
                                border = BorderStroke(1.dp, if (isEventScheduled) GoldPrimary else Color(0xFFDFCFBE))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(text = "🔔", fontSize = 14.sp)
                                        Column {
                                            Text(
                                                text = if (isEventScheduled) "Rappel WorkManager Actif" else "Programmer un rappel d'injection",
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isEventScheduled) GoldPrimary else InkText
                                            )
                                            Text(
                                                text = if (isEventScheduled) "Notification programmée en arrière-plan (cliquer pour annuler)" else "Définir une alerte locale (15m, 1h ou à l'heure)",
                                                fontSize = 10.sp,
                                                color = MutedText
                                            )
                                        }
                                    }
                                    Text(
                                        text = if (isEventScheduled) "✓ Actif" else "+ Définir",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isEventScheduled) SuccessGreen else GoldPrimary
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFCF9F5), RoundedCornerShape(10.dp))
                            .border(1.dp, BorderLine, RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "Aucun événement particulier le $selectedDay ${monthNames[currentMonth]} 2026. Sélectionnez une date encadrée pour voir les détails cliniques.",
                            fontSize = 11.5.sp,
                            color = MutedText
                        )
                    }
                }
            }
        }

        // ACTIVE WORKMANAGER REMINDERS SECTION
        ActiveRemindersSection(
            modifier = Modifier.fillMaxWidth(),
            onRemindersUpdated = { remindersRefreshKey++ }
        )

        // FULL SEPTEMBER CHRONOLOGY SECTION (Direct access to all September steps with date, time, and details)
        if (currentMonth == 9) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Toutes les étapes de Septembre 2026",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFEAF4ED),
                            border = BorderStroke(1.dp, Color(0xFFC7E2D0))
                        ) {
                            Text(
                                text = "17 ÉTAPES VALIDÉES",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                    }

                    Text(
                        text = "Du premier jour de suppression ovarienne jusqu'au déclenchement final à 23h00.",
                        fontSize = 11.5.sp,
                        color = MutedText
                    )

                    Divider(color = BorderLine)

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        eventsThisMonth.forEach { ev ->
                            SeptemberStepItem(
                                event = ev,
                                isSelected = (ev.day == selectedDay),
                                onSelect = { selectedDay = ev.day },
                                onOpenReminder = { eventForReminder = ev },
                                refreshKey = remindersRefreshKey
                            )
                        }
                    }
                }
            }
        }

        // RMA Notice
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFBF6F0),
            border = BorderStroke(1.dp, Color(0xFFDFCFBE))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(text = "🏥", fontSize = 14.sp)
                Text(
                    text = "Suivi RMA of New York & Gramercy : Chaque étape est rigoureusement coordonnée avec l'équipe médicale (échographies matinales entre 07h00 et 08h30, injections vespérales à heure fixe).",
                    fontSize = 11.sp,
                    color = Color(0xFF523F33),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun SeptemberStepItem(
    event: CalendarEventDetail,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onOpenReminder: () -> Unit,
    refreshKey: Int
) {
    val context = LocalContext.current
    val eventId = remember(event) { "evt_${event.month}_${event.day}_${event.time.filter { it.isDigit() }}" }
    val hasReminder = remember(event, refreshKey) {
        MedicationReminderScheduler.isReminderScheduled(context, eventId)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() },
        color = if (isSelected) Color(0xFFFDF8F0) else Color(0xFFFAFAFA),
        border = BorderStroke(1.dp, if (isSelected) GoldPrimary else Color(0xFFEFE9E0)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldPrimary
                    ) {
                        Text(
                            text = "${event.day} sept.",
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "⏰ ${event.time}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B584E)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    // WorkManager Reminder Button
                    Surface(
                        modifier = Modifier.clickable { 
                            if (hasReminder) {
                                MedicationReminderScheduler.cancelReminder(context, eventId)
                                Toast.makeText(context, "Rappel annulé pour le ${event.day} sept.", Toast.LENGTH_SHORT).show()
                            } else {
                                onOpenReminder()
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (hasReminder) Color(0xFF5C3B24) else Color(0xFFF4EBD8),
                        border = BorderStroke(1.dp, if (hasReminder) Color(0xFF382214) else Color(0xFFE6CA8D))
                    ) {
                        Text(
                            text = if (hasReminder) "🔔 Rappel Actif" else "🔔 Rappel",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasReminder) Color.White else Color(0xFF5C3B24)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFEAF4ED)
                    ) {
                        Text(
                            text = "✓ Fait",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SuccessGreen
                        )
                    }
                }
            }

            Text(
                text = event.title,
                fontSize = 13.5.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = InkText
            )

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "📍", fontSize = 10.sp)
                Text(
                    text = event.location,
                    fontSize = 10.5.sp,
                    color = Color(0xFF806E64)
                )
            }

            Text(
                text = event.details,
                fontSize = 11.5.sp,
                color = InkText,
                lineHeight = 15.sp
            )

            if (hasReminder) {
                val reminderInfo = remember(event, refreshKey) {
                    MedicationReminderScheduler.getReminderDetails(context, eventId)
                }
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF7EC),
                    border = BorderStroke(1.dp, Color(0xFFE6CA8D))
                ) {
                    Text(
                        text = "✓ Alerte WorkManager programmée : ${reminderInfo?.timeLabel ?: "Rappel actif"}.",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5C3B24)
                    )
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(text = label, fontSize = 10.5.sp, color = MutedText)
    }
}
