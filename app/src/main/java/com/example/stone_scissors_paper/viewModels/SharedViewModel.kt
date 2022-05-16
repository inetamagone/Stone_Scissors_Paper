package com.example.stone_scissors_paper.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.work.*
import com.example.stone_scissors_paper.data.GameData
import com.example.stone_scissors_paper.repository.ScoreRepository
import com.example.stone_scissors_paper.workers.GameWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "SharedViewModel"

class SharedViewModel(application: Application, private val repository: ScoreRepository) : AndroidViewModel(application) {

    companion object {
        const val PLAYERS_MOVE = "players_move"
        const val PHONE_MOVE = "phone_move"
    }

//    var workerScore: MutableLiveData<Int>? = null
    lateinit var outputWorkInfo: LiveData<WorkInfo>

    private val workManager = WorkManager.getInstance(application)
    private val oneTimeWorkRequest = OneTimeWorkRequest.Builder(GameWorker::class.java)

    fun playGame(playersMove: Int, phoneMove: Int): LiveData<WorkInfo> {

        val data: Data = Data.Builder()
            .putInt(PLAYERS_MOVE, playersMove)
            .putInt(PHONE_MOVE, phoneMove)
            .build()

        oneTimeWorkRequest
            .setInputData(data)
            .build()

        workManager
            .enqueue(oneTimeWorkRequest.build())

        outputWorkInfo = workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.build().id)
        Log.d(TAG, "outputWorkInfo value: $outputWorkInfo")
        return outputWorkInfo
    }

//    fun observeInViewModel(): Observer<WorkInfo> {
//        return Observer { listOfWorkInfo ->
//            if (listOfWorkInfo.state.isFinished) {
//                val scoreValue = listOfWorkInfo.outputData.getInt(GameWorker.SCORE_VALUE, 0)
//                Log.d(TAG, "scoreValue: $scoreValue")
//                setLiveData(scoreValue)
//            }
//        }
//    }
//    fun setLiveData(scoreValue: Int) {
//        workerScore?.value = scoreValue
//    }

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