package com.example.stone_scissors_paper

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stone_scissors_paper.adapters.ScoreAdapter
import com.example.stone_scissors_paper.data.GameData
import com.example.stone_scissors_paper.databinding.FragmentScoreBinding
import com.example.stone_scissors_paper.repository.ScoreRepository
import com.example.stone_scissors_paper.viewModels.SharedViewModel
import com.example.stone_scissors_paper.viewModels.ViewModelFactory

class ScoreFragment : Fragment() {

    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: ScoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.scoreRecyclerView

        val repository = ScoreRepository(requireContext())
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        viewModel.getAllScoresFromDb().observe(viewLifecycleOwner) {
            observeData(it, recyclerView)
        }

        binding.deleteAllButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                viewModel.deleteAllScores()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.score_history_deleted),
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
            builder.setTitle(getString(R.string.delete_all))
            builder.setMessage(getString(R.string.question_delete_all))
            builder.create().show()
        }
    }

    private fun observeData(it: List<GameData>, recyclerView: RecyclerView) =
        it.let {
            adapter = ScoreAdapter(requireContext(), it)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}