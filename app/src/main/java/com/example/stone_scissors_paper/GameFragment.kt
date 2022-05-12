package com.example.stone_scissors_paper

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.stone_scissors_paper.data.GameData
import com.example.stone_scissors_paper.data.ImageOption
import com.example.stone_scissors_paper.databinding.FragmentGameBinding
import com.example.stone_scissors_paper.repository.ScoreRepository
import com.example.stone_scissors_paper.viewModels.SharedViewModel
import com.example.stone_scissors_paper.viewModels.ViewModelFactory

private const val TAG = "GameFragment"

class GameFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel

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

        val repository = ScoreRepository(requireContext())
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        val playerScore = binding.firstPlayerScore
        val phoneScore = binding.secondPlayerScore

        playerScore.text = "0"
        phoneScore.text = "0"

        binding.restartButton.setOnClickListener {
            val scoreToSave = GameData(myScore = playerScore.text.toString(), phoneScore = phoneScore.text.toString())
            viewModel.insertScoreInDb(scoreToSave)

            playerScore.text = "0"
            phoneScore.text = "0"
        }

        val navController = Navigation.findNavController(view)
        binding.scoreButton.setOnClickListener {
            navController.navigate(R.id.action_gameFragment_to_scoreFragment, Bundle())
        }

        binding.playButton.setOnClickListener {

            val playersMove = (1..3).random()
            Log.d(TAG, "$playersMove")
            val phoneMove = (1..3).random()
            Log.d(TAG, "$phoneMove")

            when (getScore(playersMove, phoneMove)) {
                2 -> {
                    Log.d(TAG, "Phone wins")
                    val scoreValue = binding.secondPlayerScore.text.toString().toInt() + 1
                    binding.secondPlayerScore.text = scoreValue.toString()
                }
                3 -> {
                    Log.d(TAG, "Player wins")
                    val scoreValue = binding.firstPlayerScore.text.toString().toInt() + 1
                    binding.firstPlayerScore.text = scoreValue.toString()
                }
                else -> {
                    Log.d(TAG, "Equals")
                    val toast = Toast.makeText(
                        requireContext(),
                        getString(R.string.score_equals),
                        Toast.LENGTH_SHORT
                    )
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                }
            }

            when (playersMove) {
                1 -> {
                    binding.firstPlayerImage.setImageResource(ImageOption.PAPER.image) }
                2 -> {
                    binding.firstPlayerImage.setImageResource(ImageOption.SCISSORS.image)
                }
                else -> {
                    binding.firstPlayerImage.setImageResource(ImageOption.STONE.image)
                }
            }

            when (phoneMove) {
                1 -> {
                    binding.secondPlayerImage.setImageResource(ImageOption.PAPER.image)
                }
                2 -> {
                    binding.secondPlayerImage.setImageResource(ImageOption.SCISSORS.image)
                }
                else -> {
                    binding.secondPlayerImage.setImageResource(ImageOption.STONE.image)
                }
            }
        }
    }

    /* result = 1 - equals
        result = 2 - phone wins
        result = 3 - player wins */
    private fun getScore(playerMove: Int, phoneMove: Int): Int {
        var result = 0
        when {
            // equals
            playerMove == phoneMove -> {
                result = 1
            }
            // Phone wins
            playerMove == 1 && phoneMove == 2 || playerMove == 2 && phoneMove == 3 || playerMove == 3 && phoneMove == 1 -> {
                result = 2
            }
            // Player wins
            else -> {
                result = 3
            }
        }
        Log.d(TAG, "$result")
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}