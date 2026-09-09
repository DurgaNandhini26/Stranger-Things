package com.example.strangerthings.challenges

import android.os.Build
import android.os.Debug
import java.security.MessageDigest

object Challenge3RootDebug {

    const val CHALLENGE_NAME = "The Shadow Gate"
    const val LORE = "Three locks seal the gate. But the gate shifts with time."
    const val DESCRIPTION = "Bypass ALL THREE checks:\n- Root detection\n- Debug detection\n- Emulator detection\n\nThe flag changes every hour.\nHook getFlag() and submit within 1 hour."
    const val HINT = "Hook isRooted(), isDebuggable(), isEmulator() — all must return false. Then intercept getFlag() return value and submit."
    const val POINTS = 300

    private const val SECRET = "hawkins_gate_011"

    private const val DECOY_1 = "STF{n0t_th1s_0n3}"
    private const val DECOY_2 = "STF{k33p_try1ng}"
    private const val DECOY_3 = "STF{alm0st_th3r3}"

    fun isRooted(): Boolean {
        val paths = listOf(
            "/su", "/system/bin/su", "/system/xbin/su",
            "/sbin/su", "/data/local/su",
            "/system/sd/xbin/su", "/system/bin/failsafe/su"
        )
        return paths.any { java.io.File(it).exists() }
    }

    fun isDebuggable(): Boolean = Debug.isDebuggerConnected()

    fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.contains("emulator")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic")
                || Build.PRODUCT.contains("sdk")
    }

    private fun generateFlag(): String {
        val window = System.currentTimeMillis() / 3600000
        val raw = "$SECRET:$window"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(raw.toByteArray())
        val dynamic = hash.take(8).joinToString("") { "%02x".format(it) }
        return "STF{${dynamic}_g4t3_0p3n}"
    }

    fun getFlag(): String {
        return when {
            isRooted()     -> DECOY_1
            isDebuggable() -> DECOY_2
            isEmulator()   -> DECOY_3
            else           -> generateFlag()
        }
    }

    fun verify(input: String): Boolean {
        val currentWindow = System.currentTimeMillis() / 3600000
        val prevWindow    = currentWindow - 1

        fun flagForWindow(w: Long): String {
            val raw = "$SECRET:$w"
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(raw.toByteArray())
            val dynamic = hash.take(8).joinToString("") { "%02x".format(it) }
            return "STF{${dynamic}_g4t3_0p3n}"
        }

        return input.trim() == flagForWindow(currentWindow)
                || input.trim() == flagForWindow(prevWindow)
    }
}