package com.example.stone_scissors_paper.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stone_scissors_paper.data.GameData

@Database(
    entities = [GameData::class],
    version = 1,
    exportSchema = false
)
abstract class ScoreDatabase: RoomDatabase() {
    abstract fun getScoreDao(): ScoreDao

    companion object {
        @Volatile
        // Recreate the instance of database
        var INSTANCE: ScoreDatabase? = null

        fun createDatabase(context: Context): ScoreDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreDatabase::class.java,
                    "score_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}