package com.example.stone_scissors_paper.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stone_scissors_paper.data.GameData

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(gameData: GameData)

    @Query("SELECT * FROM game_scores ORDER BY timestamp ASC")
    fun getAllScores(): LiveData<List<GameData>>

    @Query("DELETE FROM game_scores")
    suspend fun deleteAll()
}