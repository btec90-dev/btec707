package com.example.data

data class LabResult(
    val id: String,
    val date: String,
    val testName: String, // ex. "Estradiol (E2)", "Progestérone", "Follicules", "Bêta-hCG"
    val value: Float,
    val unit: String
)
