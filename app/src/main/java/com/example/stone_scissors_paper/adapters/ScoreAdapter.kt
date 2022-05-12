package com.example.stone_scissors_paper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stone_scissors_paper.R
import com.example.stone_scissors_paper.data.GameData
import com.example.stone_scissors_paper.databinding.ListItemBinding

class ScoreAdapter(private val context: Context, private val scoreList: List<GameData>): RecyclerView.Adapter<ScoreAdapter.ScoreHolder>() {

    inner class ScoreHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(gameData: GameData) {
            binding.apply {
                scoresPlayerScore.text = context.getString(R.string.score_text, gameData.myScore.toString())
                scoresPhoneScore.text = context.getString(R.string.phone_score, gameData.phoneScore.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreAdapter.ScoreHolder {
        return ScoreHolder(ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ScoreAdapter.ScoreHolder, position: Int) {
        holder.bind(scoreList[position])
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }


}