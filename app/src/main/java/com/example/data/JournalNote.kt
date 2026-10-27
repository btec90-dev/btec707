package com.example.data

data class JournalNote(
    val id: String,
    val title: String,
    val date: String,
    val category: String, // "Émotion & Pensée", "Médical / Questions", "Jalon important"
    val content: String
)
