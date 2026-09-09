package com.example.strangerthings.challenges

object Challenge6SSLPinning {

    const val CHALLENGE_NAME = "The Lab Tapes"
    const val LORE = "Hawkins Lab encrypted their transmissions. Intercept the signal."
    const val POINTS = 600


    private val ENCODED_FLAG = byteArrayOf(
        0x69, 0x6E, 0x7C, 0x41, 0x49, 0x49, 0x56, 0x65,
        0x4A, 0x0B, 0x54, 0x54, 0x0B, 0x54, 0x5D, 0x65,
        0x58, 0x43, 0x4A, 0x0E, 0x49, 0x49, 0x09, 0x5E,
        0x65, 0x52, 0x0E, 0x4D, 0x51, 0x0B, 0x54, 0x49,
        0x47
    )
    private const val XOR_KEY = 0x3A

    private val PINNED_FINGERPRINT = "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="

    fun getFlag(): String {
        return String(ENCODED_FLAG.map { (it.toInt() xor XOR_KEY).toByte() }.toByteArray())
    }

    fun verify(input: String): Boolean {
        return input.trim() == getFlag()
    }

    fun isPinningBypassed(): Boolean = false

    fun getFlagIfBypassed(): String {
        return if (isPinningBypassed()) getFlag()
        else "STF{n0t_y3t_byp4ss3d}"
    }
}