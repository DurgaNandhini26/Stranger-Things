package com.example.strangerthings

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChallengesActivity : AppCompatActivity() {

    val challenges = listOf(
        Challenge(1, "The Hawkins Files",
            "Hopper hid the files deep in the archives.", 100),
        Challenge(2, "Eleven's Powers",
            "Eleven's power only manifests when someone is watching.",
            200),
        Challenge(3, "The Shadow Gate",
            "Three locks seal the gate.",
            300),
        Challenge(4, "Mind Flayer's Cipher",
            "The Mind Flayer encrypted its secrets.",
            400),
        Challenge(5, "The Gate Closes",
            "The final gate. The flag is locked deep in the vault.",
            500),
        Challenge(6, "The Lab Tapes",
            "Hawkins Lab encrypted their transmissions. Intercept the signal.",
            600),

        Challenge(7, "The Hive Mind",
            "The Mind Flayer hides its thoughts in the ether. Scan the memory.",
            700),

        Challenge(8, "The Upside Down Mirror",
            "The app knows when it's been tampered with. Convince it otherwise.",
            800),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenges)

        val prefs = getSharedPreferences("ctf_progress", MODE_PRIVATE)
        val score = prefs.getInt("total_score", 0)
        findViewById<TextView>(R.id.tvTotalScore).text = "Score: $score / 3000 pts"

        val rv = findViewById<RecyclerView>(R.id.rvChallenges)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = ChallengeAdapter(challenges, prefs) { challenge ->
            val intent = Intent(this, ChallengeDetailActivity::class.java)
            intent.putExtra("challenge_id", challenge.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("ctf_progress", MODE_PRIVATE)
        findViewById<TextView>(R.id.tvTotalScore).text =
            "Score: ${prefs.getInt("total_score", 0)} / 1500 pts"
        findViewById<RecyclerView>(R.id.rvChallenges).adapter?.notifyDataSetChanged()
    }
}