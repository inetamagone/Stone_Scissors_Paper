package com.example.stone_scissors_paper

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.stone_scissors_paper.databinding.FragmentGameBinding

private const val TAG = "GameFragment"

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        Log.d(TAG, "OnCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.restartButton.setOnClickListener {
            // TODO: Saves scores and restart new scores
        }

        val navController = Navigation.findNavController(view)
        binding.scoreButton.setOnClickListener {
            navController.navigate(R.id.action_gameFragment_to_scoreFragment, Bundle())
        }

        binding.playButton.setOnClickListener {
            // TODO: Action to change images, display results and scores
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}