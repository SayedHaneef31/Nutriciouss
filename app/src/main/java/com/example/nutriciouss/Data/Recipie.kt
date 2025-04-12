package com.example.nutriciouss.Data

data class Recipie(
    val id: Int,
    val title: String,
    val image: String,
    val summary: String,
    val readyInMinutes: Int,
    val healthScore: Int
)
