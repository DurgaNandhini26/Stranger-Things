package com.example.strangerthings.challenges

import android.os.Debug
import java.security.MessageDigest
import java.util.UUID

object Challenge2Frida {
    const val CHALLENGE_NAME = "Eleven's Powers"
    const val LORE = "Eleven's power only manifests when someone is watching."
    const val POINTS = 200

    private const val DECOY_1 = "STF{n0t_l1k3_th4t_us3_fr1d4}"
    private const val DECOY_2 = "STF{wr0ng_tr4ck_k33p_g01ng}"


    private val sessionToken: String = UUID.randomUUID().toString()
    private const val SECRET_SEED = "hwk1ns_m1nd_fl4y3r"

    private fun buildFlag(): String {
        val raw = sessionToken + SECRET_SEED
        val hash = MessageDigest
            .getInstance("SHA-256")
            .digest(raw.toByteArray())
        // Take first 12 bytes of hash as hex — short enough to submit
        val token = hash.take(12)
            .joinToString("") { "%02x".format(it) }
        return "STF{${token}}"
    }

    fun getElevensPower(): String {
        return if (Debug.isDebuggerConnected()) {
            DECOY_1
        } else {
            buildFlag()
        }
    }

    fun verify(input: String): Boolean {
        return input.trim() == buildFlag()
    }
}
