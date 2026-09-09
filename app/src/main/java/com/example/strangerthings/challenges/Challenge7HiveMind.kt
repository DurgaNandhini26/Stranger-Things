package com.example.strangerthings.challenges

object Challenge7HiveMind {

    const val CHALLENGE_NAME = "The Hive Mind"
    const val LORE = "The Mind Flayer hides its thoughts in the ether. Scan the memory."
    const val POINTS = 700


    private val ENCODED = byteArrayOf(
        0x78, 0x7F, 0x6D, 0x50, 0x43, 0x18, 0x1F, 0x5B,
        0x74, 0x46, 0x18, 0x46, 0x1B, 0x59, 0x52, 0x74,
        0x4D, 0x47, 0x1F, 0x52, 0x18, 0x59, 0x74, 0x4D,
        0x1B, 0x5E, 0x45, 0x4F, 0x56
    )
    private const val XOR_KEY = 0x2B


    private var _heapSecret: String? = null

    fun initSecret() {
        // Decoded only at runtime — not visible in jadx as plaintext
        _heapSecret = String(ENCODED.map { (it.toInt() xor XOR_KEY).toByte() }.toByteArray())
    }

    fun isActive(): Boolean = _heapSecret != null

    fun getSecret(): String? = _heapSecret

    fun verify(input: String): Boolean {
        if (_heapSecret == null) initSecret()
        return input.trim() == _heapSecret
    }
}