package com.example.strangerthings

import android.content.Context
import android.util.Log
import com.example.strangerthings.challenges.Challenge2Frida
import com.example.strangerthings.challenges.Challenge3RootDebug
import com.example.strangerthings.challenges.Challenge6SSLPinning
import com.example.strangerthings.challenges.Challenge7HiveMind
import com.example.strangerthings.challenges.Challenge8Signature
import org.json.JSONObject

object FlagVerifier {

    fun verify(context: Context, challengeId: Int, input: String): Boolean {
        Log.d("CTF_VERIFY", "Verifying challenge $challengeId")
        Log.d("CTF_VERIFY", "Input received: '$input'")

        return when (challengeId) {
            1 -> verifyChallenge1(context, input)
            2 -> {
                val expected = "STF{fr1d4_s33s_4ll_3l3v3n_w4tch3s}"
                Log.d("CTF_VERIFY", "Expected: '$expected'")
                Log.d("CTF_VERIFY", "Match: ${input == expected}")
                input.trim() == expected
            }
            3 -> Challenge3RootDebug.verify(input)
            4 -> {
                try {
                    val db = FlagDatabase(context)
                    // Explicitly verifying for Challenge 4
                    val result = db.verify(4, input)
                    db.close()
                    result
                } catch (e: Exception) {
                    Log.e("CTF_VERIFY", "Database Error (Ch 4): ${e.message}")
                    false
                }
            }
            5 -> {
                try {
                    val db = FlagDatabase(context)
                    // Explicitly verifying for Challenge 5
                    val result = db.verify(5, input)
                    db.close()
                    result
                } catch (e: Exception) {
                    Log.e("CTF_VERIFY", "Database Error (Ch 5): ${e.message}")
                    false
                }
            }
            6 -> Challenge6SSLPinning.verify(input)
            7 -> Challenge7HiveMind.verify(input)
            8 -> {
                val result = Challenge8Signature.verify(context, input)
                result
            }
            else -> {
                false
            }
        }
    }

    private fun verifyChallenge1(context: Context, input: String): Boolean {
        return try {
            val json = context.assets
                .open("config.json")
                .bufferedReader()
                .readText()
            val result = JSONObject(json).has(input.trim())
            result
        } catch (e: Exception) {
            Log.e("CTF_VERIFY", "Error reading config.json: ${e.message}")
            false
        }
    }
}