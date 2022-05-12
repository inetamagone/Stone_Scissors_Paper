package com.example.stone_scissors_paper

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val winnerMessage = binding.winMessage

        playerScore.text = "0"
        phoneScore.text = "0"
        winnerMessage.text = getString(R.string.win_message)

        binding.restartButton.setOnClickListener {
            val scoreToSave = GameData(
                myScore = playerScore.text.toString(),
                phoneScore = phoneScore.text.toString()
            )
            viewModel.insertScoreInDb(scoreToSave)

            playerScore.text = "0"
            phoneScore.text = "0"
            winnerMessage.text = getString(R.string.win_message)
        }

        val navController = Navigation.findNavController(view)
        binding.scoreButton.setOnClickListener {
            navController.navigate(R.id.action_gameFragment_to_scoreFragment, Bundle())
        }

        binding.paperButton.setOnClickListener {
            val playersMove = 1
            val phoneMove = (1..3).random()

            setImages(playersMove, phoneMove)
            setCurrentScore(playersMove, phoneMove)
        }
        binding.scissorsButton.setOnClickListener {
            val playersMove = 2
            val phoneMove = (1..3).random()

            setImages(playersMove, phoneMove)
            setCurrentScore(playersMove, phoneMove)
        }
        binding.stoneButton.setOnClickListener {
            val playersMove = 3
            val phoneMove = (1..3).random()

            setImages(playersMove, phoneMove)
            setCurrentScore(playersMove, phoneMove)
        }
    }

    private fun setImages(playersMove: Int, phoneMove: Int) {
        when (playersMove) {
            1 -> {
                binding.firstPlayerImage.setImageResource(ImageOption.PAPER.image)
            }
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

    // result = 1 -> equals; result = 2 -> phone wins; result = 3 -> player wins
    private fun setCurrentScore(playerMove: Int, phoneMove: Int) {
        when {
            // equals
            playerMove == phoneMove -> {
                binding.winMessage.text = getString(R.string.score_equals)
            }
            // Phone wins
            playerMove == 1 && phoneMove == 2 || playerMove == 2 && phoneMove == 3 || playerMove == 3 && phoneMove == 1 -> {
                val scoreValue = binding.secondPlayerScore.text.toString().toInt() + 1
                binding.secondPlayerScore.text = scoreValue.toString()
                binding.winMessage.text = getString(R.string.phone_wins)
            }
            // Player wins
            else -> {
                val scoreValue = binding.firstPlayerScore.text.toString().toInt() + 1
                binding.firstPlayerScore.text = scoreValue.toString()
                binding.winMessage.text = getString(R.string.you_win)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}