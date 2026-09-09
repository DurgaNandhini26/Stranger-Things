package com.example.strangerthings.challenges

import android.content.Context
import android.content.pm.PackageManager

object Challenge8Signature {

    const val CHALLENGE_NAME = "The Upside Down Mirror"
    const val LORE = "The app knows when it's been tampered with. Convince it otherwise."
    const val POINTS = 800

    private const val EXPECTED_SIG_HASH = "a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4"


    private val ENCODED_FLAG = byteArrayOf(
        0x46, 0x41, 0x53, 0x6E, 0x66, 0x24, 0x72, 0x7B,
        0x21, 0x61, 0x60, 0x67, 0x26, 0x4A, 0x63, 0x26,
        0x67, 0x24, 0x73, 0x24, 0x26, 0x71, 0x68
    )
    private const val XOR_KEY = 0x15

    private const val DECOY = "STF{wr0ng_s1gn4tur3_try_4g41n}"

    fun getActualSigHash(context: Context): String {
        return try {
            val pm = context.packageManager
            val info = pm.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            val sig = info.signatures?.get(0)?.toByteArray() ?: return "unknown"
            val md = java.security.MessageDigest.getInstance("MD5")
            md.digest(sig).joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            "unknown"
        }
    }

    fun isSignatureValid(context: Context): Boolean {
        return getActualSigHash(context) == EXPECTED_SIG_HASH
    }

    fun getFlag(context: Context): String {
        return if (isSignatureValid(context)) decodeFlag() else DECOY
    }

    private fun decodeFlag(): String {
        return String(ENCODED_FLAG.map { (it.toInt() xor XOR_KEY).toByte() }.toByteArray())
    }

    fun verify(context: Context, input: String): Boolean {
        return input.trim() == decodeFlag()
    }
}