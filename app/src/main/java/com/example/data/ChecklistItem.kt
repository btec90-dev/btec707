package com.example.data

data class ChecklistItem(
    val id: String,
    val symbol: String,
    val dateText: String,
    val badgeText: String,
    val badgeType: String, // "past", "today", "imminent", "future", "done", "updated"
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)
