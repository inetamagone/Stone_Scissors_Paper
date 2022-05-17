package com.example.stone_scissors_paper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.WorkInfo
import com.example.stone_scissors_paper.data.GameData
import com.example.stone_scissors_paper.data.ImageOption
import com.example.stone_scissors_paper.databinding.FragmentGameBinding
import com.example.stone_scissors_paper.repository.ScoreRepository
import com.example.stone_scissors_paper.viewModels.SharedViewModel
import com.example.stone_scissors_paper.viewModels.ViewModelFactory
import com.example.stone_scissors_paper.workers.GameWorker

class GameFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        val repository = ScoreRepository(requireContext())
        val factory = ViewModelFactory(this, repository)
        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        val myScore = binding.firstPlayerScore
        val phoneScore = binding.secondPlayerScore
        val winMessage = binding.winMessage

        viewModel.savedStateData.observe(viewLifecycleOwner) { savedGameScore ->
            if (savedGameScore == null) {
                myScore.text = "0"
                phoneScore.text = "0"
                winMessage.text = getString(R.string.win_message)
            } else {
                myScore.text = savedGameScore.myScore
                phoneScore.text = savedGameScore.phoneScore
                winMessage.text = savedGameScore.winnerMessage
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playerScore = binding.firstPlayerScore
        val phoneScore = binding.secondPlayerScore
        val winnerMessage = binding.winMessage

        binding.restartButton.setOnClickListener {
            val gameData = setDataForSaving(playerScore.formatting(), phoneScore.formatting(), winnerMessage.formatting())
            viewModel.insertScoreInDb(gameData)

            playerScore.text = "0"
            phoneScore.text = "0"
            winnerMessage.text = getString(R.string.win_message)
        }

        val navController = Navigation.findNavController(view)
        binding.scoreButton.setOnClickListener {
            val playersScoreToSave = playerScore.formatting()
            val phoneScoreToSave = phoneScore.formatting()
            val messageToSave = winnerMessage.formatting()
            val gameData = setDataForSaving(playersScoreToSave, phoneScoreToSave, messageToSave)

            viewModel.saveState(gameData)
            navController.navigate(R.id.action_gameFragment_to_scoreFragment, Bundle())
        }

        binding.paperButton.setOnClickListener {
            val playersMove = 1
            val phoneMove = (1..3).random()

            viewModel.playGame(requireContext(), playersMove, phoneMove)
            setImages(playersMove, phoneMove)
            viewModel.outputWorkInfo.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    onStateChange(it, binding)
                }
            })
        }

        binding.scissorsButton.setOnClickListener {
            val playersMove = 2
            val phoneMove = (1..3).random()

            viewModel.playGame(requireContext(), playersMove, phoneMove)
            setImages(playersMove, phoneMove)
            viewModel.outputWorkInfo.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    onStateChange(it, binding)
                }
            })
        }

        binding.stoneButton.setOnClickListener {
            val playersMove = 3
            val phoneMove = (1..3).random()

            viewModel.playGame(requireContext(), playersMove, phoneMove)
            setImages(playersMove, phoneMove)
            viewModel.outputWorkInfo.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    onStateChange(it, binding)
                }
            })
        }
    }

    override fun onStop() {
        super.onStop()
        val playersScoreToSave = binding.firstPlayerScore.formatting()
        val phoneScoreToSave = binding.secondPlayerScore.formatting()
        val messageToSave = binding.winMessage.formatting()
        val gameData = setDataForSaving(playersScoreToSave, phoneScoreToSave, messageToSave)

        viewModel.saveState(gameData)
    }

    private fun setDataForSaving(playerScore: String, phoneScore: String, message: String): GameData {
        return GameData(myScore = playerScore, phoneScore = phoneScore, winnerMessage = message)
    }
    
    private fun onStateChange(it: WorkInfo, binding: FragmentGameBinding) =
        binding.apply {
            val outPutData = it.outputData.getInt(GameWorker.SCORE_VALUE, 0)
            if (outPutData != 0) {
                setScore(outPutData)
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
    private fun setScore(workerScore: Int) =
        when (workerScore) {
            // Equals
            1 -> {
                binding.winMessage.text = getString(R.string.score_equals)

            } // Phone wins
            2 -> {
                val scoreValue = binding.secondPlayerScore.formatting().toInt() + 1
                binding.secondPlayerScore.text = scoreValue.toString()
                binding.winMessage.text = getString(R.string.phone_wins)
            } // Player wins
            else -> {
                val scoreValue = binding.firstPlayerScore.formatting().toInt() + 1
                binding.firstPlayerScore.text = scoreValue.toString()
                binding.winMessage.text = getString(R.string.you_win)
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun TextView?.formatting(): String =
    this?.text.toString()