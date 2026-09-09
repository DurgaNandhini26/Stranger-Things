package com.example.strangerthings.challenges

import android.content.Context
import android.util.Log
import org.json.JSONObject

object Challenge1Static {
    const val CHALLENGE_NAME = "The Hawkins Files"
    const val LORE = "Hopper hid the files deep in the archives. Secrets are buried in plain sight."
    const val POINTS = 100

    fun verify(context: Context, input: String): Boolean {
        return try {
            val json = context.assets
                .open("config.json")
                .bufferedReader()
                .readText()
            Log.d("CTF_VERIFY", "config.json content: $json")
            val obj = JSONObject(json)
            val result = obj.has(input.trim())
            Log.d("CTF_VERIFY", "Has key '$input': $result")
            result
        } catch (e: Exception) {
            Log.e("CTF_VERIFY", "Exception: ${e.message}")
            false
        }
    }
}