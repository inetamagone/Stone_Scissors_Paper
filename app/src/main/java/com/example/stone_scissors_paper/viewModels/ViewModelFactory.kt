package com.example.stone_scissors_paper.viewModels

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.stone_scissors_paper.repository.ScoreRepository

class ViewModelFactory(owner: SavedStateRegistryOwner,
                          private val scoreRepository: ScoreRepository,
                          defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = SharedViewModel(scoreRepository, SavedStateHandle()) as T
}

//class ViewModelFactory(private val scoreRepository: ScoreRepository): ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(SharedViewModel::class.java)) {
//            return SharedViewModel(scoreRepository) as T
//        }
//        throw IllegalArgumentException ("UnknownViewModel")
//    }
//}
