package com.example.stone_scissors_paper.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.stone_scissors_paper.data.GameData
import com.example.stone_scissors_paper.database.ScoreDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScoreRepository(context: Context) {

    private val database = ScoreDatabase.createDatabase(context)

    suspend fun insertScore(gameData: GameData) {
        CoroutineScope(Dispatchers.IO).launch {
            database.getScoreDao().insertScore(gameData)
        }
    }

    fun getScores(): LiveData<List<GameData>> =
        database.getScoreDao().getAllScores()

    suspend fun deleteAll() =
        database.getScoreDao().deleteAll()

}