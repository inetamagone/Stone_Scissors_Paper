package com.example.stone_scissors_paper.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stone_scissors_paper.repository.ScoreRepository

class ViewModelFactory(private val scoreRepository: ScoreRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(scoreRepository) as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}
