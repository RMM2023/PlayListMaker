package com.practicum.playlistmaker.presentation.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.CURRENT_TRACK
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.presentation.ui.activity.AudioPlayerActivity
import com.practicum.playlistmaker.presentation.ui.adapter.TrackAdapter
import com.practicum.playlistmaker.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private var searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupListeners()
        observeViewModel()

        viewModel.loadSearchHistory()
    }

    private fun setupRecyclerViews() {
        adapter = TrackAdapter(mutableListOf()) { track ->
            viewModel.addTrackToHistory(track)
            openAudioPlayer(track)
        }
        binding.trackRecycler.adapter = adapter
        binding.trackRecycler.layoutManager = LinearLayoutManager(requireContext())

        historyAdapter = TrackAdapter(mutableListOf()) { track ->
            openAudioPlayer(track)
        }
        binding.searchHistoryRecycleView.adapter = historyAdapter
        binding.searchHistoryRecycleView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListeners() {

        binding.searchClearButton.setOnClickListener {
            binding.searchEditText.setText("")
            hideKeyboard()
            binding.searchEditText.clearFocus()
        }

        binding.historyClearButton.setOnClickListener {
            viewModel.clearSearchHistory()
        }

        binding.updateButton.setOnClickListener {
            viewModel.search(binding.searchEditText.text.toString())
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchClearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                updateUIVisibility(!s.isNullOrEmpty())

                searchRunnable?.let { searchHandler.removeCallbacks(it) }

                if (s.isNullOrEmpty()) {
                    viewModel.clearResults()
                    viewModel.loadSearchHistory()
                    binding.NotFoundLayout.visibility = View.GONE
                    viewModel.setLoading(false)
                } else {
                    searchRunnable = Runnable {
                        viewModel.setLoading(true)
                        binding.trackRecycler.visibility = View.GONE
                        viewModel.search(s.toString())
                    }
                    searchHandler.postDelayed(searchRunnable!!, 2000L) // Изменено с 500L на 2000L
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { tracks ->
            adapter.updateList(tracks.toMutableList())
            updateUIVisibility(tracks.isNotEmpty())
        }

        viewModel.searchHistory.observe(viewLifecycleOwner) { history ->
            historyAdapter.updateList(history.toMutableList())
            binding.searchHistoryLayout.visibility = if (history.isNotEmpty() && binding.searchEditText.text.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                binding.NotFoundLayout.visibility = View.GONE
                binding.NotConnectedLayout.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            updateUIVisibility(errorMessage == null)
        }
    }

    private fun updateUIVisibility(hasResults: Boolean) {
        binding.trackRecycler.visibility = if (hasResults) View.VISIBLE else View.GONE
        binding.NotFoundLayout.visibility = if (!hasResults && viewModel.errorMessage.value == null) View.VISIBLE else View.GONE
        binding.NotConnectedLayout.visibility = if (!hasResults && viewModel.errorMessage.value != null) View.VISIBLE else View.GONE
        binding.searchHistoryLayout.visibility = if (!hasResults && binding.searchEditText.text.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun openAudioPlayer(track: Track) {
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
        intent.putExtra(CURRENT_TRACK, Gson().toJson(track))
        startActivity(intent)
    }
}