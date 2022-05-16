package com.example.stone_scissors_paper.viewModels

import android.content.Context
import androidx.lifecycle.*
import androidx.work.*
import com.example.stone_scissors_paper.data.GameData
import com.example.stone_scissors_paper.repository.ScoreRepository
import com.example.stone_scissors_paper.workers.GameWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(private val repository: ScoreRepository) : ViewModel() {

    companion object {
        const val PLAYERS_MOVE = "players_move"
        const val PHONE_MOVE = "phone_move"
    }

    lateinit var outputWorkInfo: LiveData<WorkInfo>

    fun playGame(context: Context, playersMove: Int, phoneMove: Int): LiveData<WorkInfo> {

        val workManager = WorkManager.getInstance(context)
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(GameWorker::class.java)

        val data: Data = Data.Builder()
            .putInt(PLAYERS_MOVE, playersMove)
            .putInt(PHONE_MOVE, phoneMove)
            .build()

        val myRequest = oneTimeWorkRequest
            .setInputData(data)
            .build()

        workManager
            .enqueueUniqueWork("my_unique_work", ExistingWorkPolicy.KEEP, myRequest)

        outputWorkInfo = workManager
            .getWorkInfoByIdLiveData(myRequest.id)
        return outputWorkInfo
    }

    fun insertScoreInDb(gameData: GameData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertScore(gameData)
        }
    }

    fun getAllScoresFromDb(): LiveData<List<GameData>> =
        repository.getScores()

    fun deleteAllScores() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }

}