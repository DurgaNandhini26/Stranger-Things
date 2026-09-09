package com.example.strangerthings

import android.R

data class Challenge(
    val id: Int,
    val name: String,
    val lore: String,
    val points: Int,
    val flag: String = ""
)