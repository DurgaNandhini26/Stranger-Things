package com.example.strangerthings

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import android.util.Log
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class FlagDatabase(context: Context) :
    SQLiteOpenHelper(context, "hawkins.db", null, 4) {

    companion object {
        private val K1 = charArrayOf('h','a','w','k','i','n','s')
        private val K2 = charArrayOf('_','l','a','b','_')
        private const val K3 = "k3y!"
        fun buildKey(): String = String(K1) + String(K2) + K3
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS vault (" +
                    "id INTEGER PRIMARY KEY," +
                    "label TEXT," +
                    "payload TEXT)"
        )

        insertFlag(db, 4, "ch4_payload", "iT37+dXD2zhRgce+82YryMAA/hIXB9Y4dSr0pYr5zGU=")

        insertFlag(db, 5, "ch5_payload", "U1RGe2Q0dGFiNHMzX2ZsNGdfZjB1bmRfeTB1fQ==")
    }

    private fun insertFlag(db: SQLiteDatabase, id: Int, label: String, payload: String) {
        val cv = ContentValues().apply {
            put("id", id)
            put("label", label)
            put("payload", payload)
        }
        db.insertWithOnConflict("vault", null, cv, SQLiteDatabase.CONFLICT_IGNORE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS vault")
        onCreate(db)
    }

    // Challenge 4 — AES decrypt then return
    fun getDecryptedFlag4(): String {
        return try {
            val cursor = readableDatabase.rawQuery(
                "SELECT payload FROM vault WHERE id=?", arrayOf("4")
            )
            if (!cursor.moveToFirst()) {
                cursor.close()
                Log.e("CTF_DB", "No row for id=4")
                return ""
            }
            val payload = cursor.getString(0)
            cursor.close()
            val key = buildKey().toByteArray(Charsets.UTF_8)
            val keySpec = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val decrypted = String(
                cipher.doFinal(Base64.decode(payload, Base64.DEFAULT))
            ).trim()
            decrypted
        } catch (e: Exception) {
            Log.e("CTF_DB", "Ch4 error: ${e.message}")
            ""
        }
    }

    fun getDecodedFlag5(): String {
        return try {
            val cursor = readableDatabase.rawQuery(
                "SELECT payload FROM vault WHERE id=?", arrayOf("5")
            )
            if (!cursor.moveToFirst()) {
                cursor.close()
                Log.e("CTF_DB", "No row for id=5")
                return ""
            }
            val payload = cursor.getString(0)
            cursor.close()
            val decoded = String(
                Base64.decode(payload, Base64.DEFAULT),
                Charsets.UTF_8
            ).trim()
            decoded
        } catch (e: Exception) {
            Log.e("CTF_DB", "Ch5 error: ${e.message}")
            ""
        }
    }

    fun verify(id: Int, input: String): Boolean {
        val expected = when (id) {
            4 -> getDecryptedFlag4()
            5 -> getDecodedFlag5()
            else -> ""
        }
        val cleanInput = input.trim()
        return cleanInput == expected
    }
}