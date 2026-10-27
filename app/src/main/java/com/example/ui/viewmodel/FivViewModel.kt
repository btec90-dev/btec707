package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.ChecklistItem
import com.example.data.JournalNote
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
                description = "Formulaire soumis via le portail avec préférence pour un garçon (« Male »). Le laboratoire sélectionnera le blastocyste euploïde masculin optimal.",
                isCompleted = true
            ),
            ChecklistItem(
                id = "chk-pgta",
                symbol = "🧬",
                dateText = "25 octobre · Reçu ✓",
                badgeText = "✓ 5 Normaux",
                badgeType = "past",
                title = "Résultats PGT-A reçus (5 euploïdes / 8)",
                description = "Analyse génétique reçue le 25 oct. : 5 embryons normaux (euploïdes) sur les 8 blastocystes biopsiés.",
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
                description = "RMA East Side (635 Madison Ave). Examen pour évaluer la cavité utérine et l'endomètre.",
                isCompleted = false
            ),
            ChecklistItem(
                id = "chk-hystero",
                symbol = "🔬",
                dateText = "30 octobre · Intervention",
                badgeText = "⏳ J-2 jours",
                badgeType = "imminent",
                title = "Hystéroscopie opératoire (retrait de débris)",
                description = "Gramercy Surgery Center (380 2nd Ave, 10th Fl). Intervention pour préparer une cavité utérine optimale avant le FET.",
                isCompleted = false
            ),
            ChecklistItem(
                id = "chk-call",
                symbol = "☎️",
                dateText = "Début novembre · après chirurgie",
                badgeText = "⏳ Prochainement",
                badgeType = "future",
                title = "Planifier l’appel de protocole FET",
                description = "Confirmer la date définitive du transfert d’embryon congelé avec l'équipe médicale (Dr Brauer).",
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
                id = "memo-4",
                title = "Résultats PGT-A reçus : 5 embryons normaux !",
                date = "2026-10-25",
                category = "Jalon important ✓",
                content = "[Immense joie ✨] Reçu les résultats PGT-A aujourd'hui : 5 embryons normaux (euploïdes) sur les 8 blastocystes biopsiés. Prêt pour la suite avec notre petit garçon !"
            ),
            JournalNote(
                id = "memo-3",
                title = "En attente douce des résultats PGT-A",
                date = "2026-10-26",
                category = "Émotion & Pensée",
                content = "[Pleine d'espoir ✨] Chaque jour nous rapproche. Rester sereine, respirer profondément, et faire confiance au travail remarquable du laboratoire RMA."
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

}
