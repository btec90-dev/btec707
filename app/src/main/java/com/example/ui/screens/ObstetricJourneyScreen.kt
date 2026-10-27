package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

data class ObstetricVisitItem(
    val weeksText: String,
    val dateText: String,
    val title: String,
    val location: String,
    val description: String,
    val exams: List<String>,
    val isMajor: Boolean = false
)

@Composable
fun ObstetricJourneyScreen() {
    val scrollState = rememberScrollState()

    val visits = listOf(
        ObstetricVisitItem(
            weeksText = "Semaines 8 à 10",
            dateText = "Décembre 2026",
            title = "Graduation RMA & 1ère Consultation OBGYN",
            location = "Cabinet du Gynécologue-Obstétricien",
            description = "Transition officielle du centre de fertilité vers le suivi de grossesse standard. Bilan de sortie RMA, transmission du dossier médical complet, et arrêt progressif sous surveillance du soutien hormonal (progestérone / œstrogènes).",
            exams = listOf("Échographie de transition", "Examen clinique complet", "Arrêt progressif des hormones de soutien"),
            isMajor = true
        ),
        ObstetricVisitItem(
            weeksText = "Semaines 11 à 13",
            dateText = "Fin Déc. 2026 – Début Janv. 2027",
            title = "Échographie de Clarté Nucale (NT Scan) & Dépistage 1er Trimestre",
            location = "Centre d'imagerie obstétricale / Hôpital",
            description = "Échographie morphologique précoce pour mesurer la nuque du fœtus, vérifier la présence des os nasaux et exclure les anomalies chromosomiques majeures, combinée à une prise de sang (marqueurs sériques).",
            exams = listOf("Échographie NT (Clarté nucale)", "Prise de sang de dépistage prénatal", "Évaluation du risque combiné")
        ),
        ObstetricVisitItem(
            weeksText = "Semaines 16 à 18",
            dateText = "Février 2027",
            title = "Visite de routine du 2ème Trimestre & Échographie de contrôle",
            location = "Cabinet OBGYN",
            description = "Consultation mensuelle standard : écoute des bruits du cœur fœtal (Doppler), mesure de la hauteur utérine, contrôle de la tension artérielle et du poids. Discussion des premiers mouvements de bébé.",
            exams = listOf("Doppler cardiaque fœtal", "Mesure hauteur utérine", "Contrôle tension & analyse d'urines")
        ),
        ObstetricVisitItem(
            weeksText = "Semaines 20 à 22",
            dateText = "Février – Mars 2027",
            title = "Grande Échographie Morphologique (Anatomy Scan)",
            location = "Centre d'imagerie obstétricale spécialisé",
            description = "L'examen clé de la grossesse ! Examen minutieux de chaque organe du bébé (cerveau, cœur, reins, colonne, membres), vérification du placenta, de la quantité de liquide amniotique et de la longueur du col utérin.",
            exams = listOf("Échographie morphologique détaillée (3D/4D possible)", "Mesure du col utérin (longueur)", "Vérification emplacement placenta")
        ),
        ObstetricVisitItem(
            weeksText = "Semaines 24 à 28",
            dateText = "Mars – Avril 2027",
            title = "Test de Glycémie (HGPO) & Visite du 6ème mois",
            location = "Laboratoire d'analyses & Cabinet OBGYN",
            description = "Dépistage du diabète gestationnel (test d'hyperglycémie provoquée par voie orale avec glucose 50g ou 75g). Si Fatou est de rhesus négatif, injection de Rhogam préventive.",
            exams = listOf("Test HGPO (Glycémie post-glucose)", "Bilan sanguin NFS / Ferritine", "Injection de Rhogam (si nécessaire)")
        ),
        ObstetricVisitItem(
            weeksText = "Semaines 30 à 32",
            dateText = "Avril 2027",
            title = "Échographie de Croissance du 3ème Trimestre",
            location = "Centre d'imagerie obstétricale",
            description = "Vérification de la courbe de croissance fœtale (poids estimé, diamètres bipariétaux), de la vascularisation utérine (Doppler ombilical) et de la position du bébé (tête en bas / siège).",
            exams = listOf("Échographie de croissance fœtale", "Doppler ombilical et utérin", "Estimation du poids de naissance")
        ),
        ObstetricVisitItem(
            weeksText = "Semaines 34 à 36",
            dateText = "Mai 2027",
            title = "Consultations bimensuelles & Dépistage GBS",
            location = "Cabinet OBGYN & Maternité",
            description = "Les visites deviennent bi-mensuelles. Prélèvement vaginal pour le dépistage du streptocoque du groupe B (GBS). Discussion approfondie du plan de naissance et des signes de début de travail.",
            exams = listOf("Prélèvement vaginal GBS", "Monitoring fœtal (RCF) de routine", "Élaboration du plan d'accouchement")
        ),
        ObstetricVisitItem(
            weeksText = "Semaines 37 à 40+",
            dateText = "Juin 2027",
            title = "Visites hebdomadaires & ARRIVÉE DE BÉBÉ 🎉",
            location = "Maternité / Hôpital d'accouchement",
            description = "Suivi hebdomadaires avec contrôles du col et monitoring (RCF). Attente sereine du déclenchement naturel du travail ou de la date terme. C'est le grand moment de l'accueil de votre bébé !",
            exams = listOf("Examens du col hebdomadaires", "Monitorets de bien-être fœtal (NST)", "Accouchement et premiers câlins en peau à peau ♡"),
            isMajor = true
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFBF6EE))
                .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "PARCOURS OBSTÉTRICAL & SUIVI GROSSESSE",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldPrimary,
                    letterSpacing = 1.4.sp
                )
                Text(
                    text = "De la Graduation RMA à la Naissance",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = InkText
                )
                Text(
                    text = "Calendrier détaillé des visites médicales, examens clés, échographies et conseils trimestre par trimestre jusqu'en Juin 2027.",
                    fontSize = 12.sp,
                    color = MutedText,
                    lineHeight = 17.sp
                )
            }
        }

        // Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "💡 Ce qui change après RMA :",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5C3B24)
                )
                Text(
                    text = "• Rythme des visites : De mensuel au 1er/2ème trimestre à hebdomadaire à l'approche du terme.\n• Interlocuteur : Votre gynécologue obstétricien (OBGYN) et l'équipe de la maternité choisie.\n• Autonomie : Surveillance des mouvements fœtaux quotidiens dès 24-28 semaines.",
                    fontSize = 11.5.sp,
                    color = InkText,
                    lineHeight = 16.sp
                )
            }
        }

        // Visits Timeline
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            visits.forEach { visit ->
                ObstetricVisitCard(visit = visit)
            }
        }
    }
}

@Composable
fun ObstetricVisitCard(visit: ObstetricVisitItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (visit.isMajor) Color(0xFFFAF4EC) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (visit.isMajor) 1.5.dp else 1.dp,
            color = if (visit.isMajor) GoldPrimary else BorderLine
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (visit.isMajor) GoldPrimary else Color(0xFFF3EAD9)
                ) {
                    Text(
                        text = visit.weeksText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (visit.isMajor) Color.White else Color(0xFF5C3B24)
                    )
                }

                Text(
                    text = "📅 ${visit.dateText}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldSecondary
                )
            }

            Text(
                text = visit.title,
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = InkText
            )

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "📍", fontSize = 11.sp)
                Text(
                    text = visit.location,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF7A685D)
                )
            }

            Divider(color = Color(0xFFEFE6DB), thickness = 0.8.dp)

            Text(
                text = visit.description,
                fontSize = 11.5.sp,
                color = InkText,
                lineHeight = 16.sp
            )

            // Exams bullet list
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = "Examens et actes clés :",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5C3B24)
                )
                visit.exams.forEach { exam ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary)
                        )
                        Text(
                            text = exam,
                            fontSize = 11.sp,
                            color = MutedText
                        )
                    }
                }
            }
        }
    }
}
