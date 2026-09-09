package com.example.strangerthings

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChallengeDetailActivity : AppCompatActivity() {

    private val challenges = listOf(
        Challenge(
            id = 1,
            name = "The Hawkins Files",
            lore = "Hopper hid the files deep in the archives. Secrets buried in plain sight.",
            points = 100
        ),
        Challenge(
            id = 2,
            name = "Eleven's Powers",
            lore = "Eleven's power only manifests when someone is watching.",
            points = 200
        ),
        Challenge(
            id = 3,
            name = "The Shadow Gate",
            lore = "Three locks seal the gate. Root, Debug, and Emulator detection guard the secret.",
            points = 300
        ),
        Challenge(
            id = 4,
            name = "Mind Flayer's Cipher",
            lore = "The Mind Flayer encrypted its secrets before retreating.",
            points = 400
        ),
        Challenge(
            id = 5,
            name = "The Gate Closes",
            lore = "The final gate. The flag is locked deep in the vault.",
            points = 500
        ),
        Challenge(
            id = 6,
            name = "SSL Pinning",
            lore = "The network hides its secrets behind a pinned certificate.",
            points = 600
        ),
        Challenge(
            id = 7,
            name = "Hive Mind",
            lore = "The Mind Flayer controls everything from the shadows.",
            points = 700
        ),
        Challenge(
            id = 8,
            name = "The Signed Gate",
            lore = "The app knows if it has been tampered with.",
            points = 800
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_detail)

        val id = intent.getIntExtra("challenge_id", 1)
        Log.d("CTF_DEBUG", "Opened challenge id: $id")

        val challenge = challenges.first { it.id == id }
        val prefs = getSharedPreferences("ctf_progress", MODE_PRIVATE)

        val tvName    = findViewById<TextView>(R.id.tvChalName)
        val tvLore    = findViewById<TextView>(R.id.tvLore)
        val tvDesc    = findViewById<TextView>(R.id.tvChalDesc)
        val tvPoints  = findViewById<TextView>(R.id.tvChalPoints)
        val etFlag    = findViewById<EditText>(R.id.etFlag)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val tvResult  = findViewById<TextView>(R.id.tvResult)
        val tvScore   = findViewById<TextView>(R.id.tvTotalScore)

        tvName.text   = challenge.name
        tvLore.text   = "\"${challenge.lore}\""
        tvPoints.text = "${challenge.points} pts"
        tvScore.text  = "Your Score: ${prefs.getInt("total_score", 0)} pts"

        // Show already solved message but keep input active
        val solved = prefs.getBoolean("solved_${challenge.id}", false)
        if (solved) {
            tvResult.text = "✅ Already solved! +${challenge.points} pts earned."
            tvResult.setTextColor(Color.parseColor("#00CC66"))
        }

        btnSubmit.setOnClickListener {
            val input = etFlag.text.toString().trim()

            Log.d("CTF_DEBUG", "Challenge ID: ${challenge.id}")
            Log.d("CTF_DEBUG", "Input: '$input'")
            Log.d("CTF_DEBUG", "Input length: ${input.length}")

            val correct = FlagVerifier.verify(this, challenge.id, input)
            Log.d("CTF_DEBUG", "Verify result: $correct")

            if (correct) {
                if (!prefs.getBoolean("solved_${challenge.id}", false)) {
                    val newScore = prefs.getInt("total_score", 0) + challenge.points
                    prefs.edit()
                        .putBoolean("solved_${challenge.id}", true)
                        .putInt("total_score", newScore)
                        .apply()

                    tvResult.text =
                        "🎉 Correct! +${challenge.points} pts!\nTotal: $newScore / 1500"
                    tvResult.setTextColor(Color.parseColor("#00CC66"))
                    tvScore.text = "Your Score: $newScore pts"

                    val allSolved = (1..5).all {
                        prefs.getBoolean("solved_$it", false)
                    }
                    if (allSolved) {
                        tvResult.append("\n\n🏆 ALL CHALLENGES COMPLETE!\nYou closed the gate.")
                    }
                } else {
                    tvResult.text = "✅ Already solved! +${challenge.points} pts earned."
                    tvResult.setTextColor(Color.parseColor("#00CC66"))
                }
            } else {
                tvResult.text = "❌ Wrong flag. The Demogorgon grows stronger."
                tvResult.setTextColor(Color.parseColor("#CC0000"))
            }
        }
    }
}