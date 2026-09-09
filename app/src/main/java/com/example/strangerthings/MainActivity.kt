package com.example.strangerthings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Reset all progress every time app launches
        getSharedPreferences("ctf_progress", MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

        // Safely init DB
        try {
            FlagDatabase(this).writableDatabase.close()
            Log.d("CTF", "DB initialized OK")
        } catch (e: Exception) {
            Log.e("CTF", "DB init failed: ${e.message}")
        }

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            startActivity(Intent(this, ChallengesActivity::class.java))
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}