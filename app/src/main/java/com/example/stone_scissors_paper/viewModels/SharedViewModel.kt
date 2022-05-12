package com.example.stone_scissors_paper.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stone_scissors_paper.data.GameData
import com.example.stone_scissors_paper.repository.ScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(private val repository: ScoreRepository): ViewModel() {

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