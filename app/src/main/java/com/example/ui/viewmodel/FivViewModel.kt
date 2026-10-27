package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.ChecklistItem
import com.example.data.JournalNote
import com.example.data.LabResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FivViewModel : ViewModel() {

    private val _currentTab = MutableStateFlow("home")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    fun setTab(tab: String) {
        _currentTab.value = tab
    }

    // Checklist state
    private val _checklistItems = MutableStateFlow(
        listOf(
            ChecklistItem(
                id = "chk-defrost",
                symbol = "🧊",
                dateText = "13 octobre · soumis",
                badgeText = "✓ Validé",
                badgeType = "past",
                title = "Plan de décongélation terminé",
                description = "Formulaire indiquant une préférence pour un garçon (« Male »). Sous réserve des résultats PGT-A.",
                isCompleted = true
            ),
            ChecklistItem(
                id = "chk-nortrel",
                symbol = "💊",
                dateText = "Chaque jour · selon prescription",
                badgeText = "⏱️ En cours",
                badgeType = "today",
                title = "Maintien de la médication quotidienne",
                description = "Continuer le Nortrel selon les instructions actuelles de l’équipe RMA.",
                isCompleted = false
            ),
            ChecklistItem(
                id = "chk-sis",
                symbol = "🏥",
                dateText = "28 octobre · 08 h 00",
                badgeText = "⏳ AUJOURD'HUI",
                badgeType = "today",
                title = "Hystérosonographie par perfusion de sérum physiologique (SIS)",
                description = "RMA East Side. Examen pour évaluer la cavité utérine et l'endomètre.",
                isCompleted = false
            ),
            ChecklistItem(
                id = "chk-hystero",
                symbol = "🔬",
                dateText = "30 octobre · Intervention chirurgicale",
                badgeText = "⏳ J-2 jours",
                badgeType = "imminent",
                title = "Hystéroscopie opératoire (retrait de débris)",
                description = "Gramercy Surgery Center. Intervention pour préparer une cavité utérine optimale avant le FET.",
                isCompleted = false
            ),
            ChecklistItem(
                id = "chk-pgta",
                symbol = "🧬",
                dateText = "23–28 octobre · fenêtre estimée",
                badgeText = "⏳ En cours",
                badgeType = "today",
                title = "Surveiller les résultats PGT-A",
                description = "Vérifier les messages du portail patient ; contacter RMA dès notification.",
                isCompleted = false
            ),
            ChecklistItem(
                id = "chk-call",
                symbol = "☎️",
                dateText = "Début novembre · après chirurgie",
                badgeText = "⏳ Prochainement",
                badgeType = "future",
                title = "Planifier l’appel de protocole FET",
                description = "Confirmer la date définitive du transfert d’embryon avec l'équipe médicale.",
                isCompleted = false
            )
        )
    )
    val checklistItems: StateFlow<List<ChecklistItem>> = _checklistItems.asStateFlow()

    fun toggleChecklistItem(id: String) {
        _checklistItems.update { list ->
            list.map { item ->
                if (item.id == id) {
                    val newCompleted = !item.isCompleted
                    item.copy(
                        isCompleted = newCompleted,
                        badgeText = if (newCompleted) "✓ RÉALISÉ" else item.badgeText
                    )
                } else item
            }
        }
    }

    // Journal notes state
    private val _journalNotes = MutableStateFlow(
        listOf(
            JournalNote(
                id = "memo-3",
                title = "En attente douce des résultats PGT-A",
                date = "2026-10-26",
                category = "Émotion & Pensée",
                content = "[Pleine d'espoir ✨] Chaque jour nous rapproche. Rester sereine, respirer profondément, et faire confiance au travail remarquable du laboratoire."
            ),
            JournalNote(
                id = "memo-2",
                title = "Préparation SIS du 28 octobre & questions Gramercy",
                date = "2026-10-24",
                category = "Médical / Questions",
                content = "[Sereine 🌸] Rendez-vous confirmé à 08h00 à RMA East Side pour l'hystérosonographie. Vérifier les consignes de jeûne pour l'intervention chirurgicale à Gramercy."
            ),
            JournalNote(
                id = "memo-1",
                title = "Après la ponction : 42 ovocytes !",
                date = "2026-10-01",
                category = "Jalon important",
                content = "[Pleine d'espoir ✨] Étape majeure franchie. 42 ovocytes prélevés, 34 matures et 8 magnifiques blastocystes vitrifiés. Très fière du chemin parcouru ♡"
            )
        )
    )
    val journalNotes: StateFlow<List<JournalNote>> = _journalNotes.asStateFlow()

    fun addNote(title: String, date: String, category: String, content: String) {
        val newNote = JournalNote(
            id = "note-" + System.currentTimeMillis(),
            title = title,
            date = date,
            category = category,
            content = content
        )
        _journalNotes.update { listOf(newNote) + it }
    }

    fun updateNote(id: String, title: String, date: String, category: String, content: String) {
        _journalNotes.update { list ->
            list.map { note ->
                if (note.id == id) {
                    note.copy(title = title, date = date, category = category, content = content)
                } else note
            }
        }
    }

    fun deleteNote(id: String) {
        _journalNotes.update { list -> list.filter { it.id != id } }
    }

    // Lab results state
    private val _labResults = MutableStateFlow(
        listOf(
            LabResult("lab-1", "23 sept.", "Estradiol (E2)", 480f, "pg/mL"),
            LabResult("lab-2", "23 sept.", "Follicules", 18f, "unités"),
            LabResult("lab-3", "27 sept.", "Estradiol (E2)", 1450f, "pg/mL"),
            LabResult("lab-4", "27 sept.", "Follicules", 32f, "unités"),
            LabResult("lab-5", "1 oct.", "Estradiol (E2)", 3200f, "pg/mL"),
            LabResult("lab-6", "1 oct.", "Follicules prélevés", 42f, "unités")
        )
    )
    val labResults: StateFlow<List<LabResult>> = _labResults.asStateFlow()

    fun addLabResult(date: String, testName: String, value: Float, unit: String) {
        val res = LabResult(
            id = "lab-" + System.currentTimeMillis(),
            date = date,
            testName = testName,
            value = value,
            unit = unit
        )
        _labResults.update { listOf(res) + it }
    }

    fun deleteLabResult(id: String) {
        _labResults.update { list -> list.filter { it.id != id } }
    }
}
