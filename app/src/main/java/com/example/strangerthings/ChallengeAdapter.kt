package com.example.strangerthings

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChallengeAdapter(
    private val challenges: List<Challenge>,
    private val prefs: SharedPreferences,
    private val onClick: (Challenge) -> Unit
) : RecyclerView.Adapter<ChallengeAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val badge: TextView = view.findViewById(R.id.tvLevelBadge)
        val name: TextView = view.findViewById(R.id.tvChallengeName)
        val type: TextView = view.findViewById(R.id.tvChallengeType)
        val points: TextView = view.findViewById(R.id.tvPoints)
        val status: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge, parent, false)
        return VH(view)
    }

    override fun getItemCount() = challenges.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = challenges[position]
        val solved = prefs.getBoolean("solved_${c.id}", false)

        holder.badge.text = c.id.toString()
        holder.name.text = c.name
        holder.points.text = "${c.points} pts"
        holder.status.text = if (solved) "✅" else "🔴"
        holder.itemView.alpha = if (solved) 0.6f else 1.0f
        holder.itemView.setOnClickListener { onClick(c) }
    }
}