package com.example.stone_scissors_paper.workers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.stone_scissors_paper.viewModels.SharedViewModel

private const val TAG = "GameWorker"

class GameWorker(val context: Context, params: WorkerParameters): Worker(context, params) {

    companion object{
        const val SCORE_VALUE = "score_value"
    }

    override fun doWork(): Result {

        return try {
            val playersMove = inputData.getInt(SharedViewModel.PLAYERS_MOVE,0)
            val phoneMove = inputData.getInt(SharedViewModel.PHONE_MOVE,0)
            val scoreValue = setCurrentScore(playersMove, phoneMove)

            val outPutData = Data.Builder()
                .putInt(SCORE_VALUE, scoreValue)
                .build()
            Log.d(TAG, "ScoreValueToPass: $scoreValue")
            Log.d(TAG, "OutPutDataToPass: $outPutData")
            return Result.success(outPutData)

        } catch (throwable: Throwable) {
            Log.e(TAG, "Error in catch")
            throwable.printStackTrace()
            Result.failure()
        }
    }

    private fun setCurrentScore(playerMove: Int, phoneMove: Int): Int {
        val scoreValue: Int = when {
            // equals
            playerMove == phoneMove -> {
                1
            }
            // Phone wins
            playerMove == 1 && phoneMove == 2 || playerMove == 2 && phoneMove == 3 || playerMove == 3 && phoneMove == 1 -> {
                2
            }
            // Player wins
            else -> {
                3
            }
        }
        return scoreValue
    }
}