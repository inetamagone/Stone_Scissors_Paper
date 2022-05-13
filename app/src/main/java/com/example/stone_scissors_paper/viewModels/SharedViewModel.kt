package com.example.stone_scissors_paper.viewModels

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

class SharedViewModel(private val repository: ScoreRepository) : ViewModel() {

    companion object {
        const val PLAYERS_MOVE = "players_move"
        const val PHONE_MOVE = "phone_move"
    }

    var workerScore: MutableLiveData<Int>? = null

    fun playGame(context: Context, playersMove: Int, phoneMove: Int) {
        val workManager = WorkManager.getInstance(context)

        val data: Data = Data.Builder()
            .putInt(PLAYERS_MOVE, playersMove)
            .putInt(PHONE_MOVE, phoneMove)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(GameWorker::class.java)

        oneTimeWorkRequest
            .setInputData(data)
            .build()

        workManager
            .enqueue(oneTimeWorkRequest.build())

//        val mObserver = Observer<WorkInfo> { myWorkInfo ->
//            // do something with myString
//        }
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.build().id)
            .observeForever {
                val scoreValue = it.outputData.getInt(GameWorker.SCORE_VALUE, 0)
                setWorkerScore(scoreValue)
                Log.d(TAG, "it: $it")
                Log.d(TAG, "scoreValue: $scoreValue")
            }
        // TODO: .removeObserver(Observer)
    }

    private fun setWorkerScore(scoreValue: Int) {
        workerScore?.value = scoreValue
        //workerScore?.removeObserver(this)
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