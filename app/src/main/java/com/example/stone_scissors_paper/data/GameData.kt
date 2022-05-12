package com.example.stone_scissors_paper.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "game_scores"
)
data class GameData(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var timestamp: Long = System.currentTimeMillis()/1000,
    @ColumnInfo(name = "my_score")
    var myScore: Int,
    @ColumnInfo(name = "phone_score")
    var phoneScore: Int
)