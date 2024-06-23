package com.practicum.playlistmaker.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.CURRENT_TRACK
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.presentation.ui.adapter.TrackAdapter
import com.practicum.playlistmaker.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.presentation.viewmodel.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerViews()
        setupListeners()
        observeViewModel()

        viewModel.loadSearchHistory()
    }

    private fun setupViewModel() {
        val factory = SearchViewModelFactory(
            Creator.provideSearchTracksUseCase(),
            Creator.provideGetSearchHistoryUseCase(applicationContext),
            Creator.provideAddTrackToHistoryUseCase(applicationContext),
            Creator.provideClearSearchHistoryUseCase(applicationContext)
        )
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
    }

    private fun setupRecyclerViews() {
        adapter = TrackAdapter(mutableListOf()) { track ->
            viewModel.addTrackToHistory(track)
            openAudioPlayer(track)
        }
        binding.trackRecycler.adapter = adapter
        binding.trackRecycler.layoutManager = LinearLayoutManager(this)

        historyAdapter = TrackAdapter(mutableListOf()) { track ->
            openAudioPlayer(track)
        }
        binding.searchHistoryRecycleView.adapter = historyAdapter
        binding.searchHistoryRecycleView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupListeners() {
        binding.searchBackButton.setOnClickListener { finish() }

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
                if (s.isNullOrEmpty()) {
                    viewModel.clearSearchHistory()
                } else {
                    viewModel.search(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(this) { tracks ->
            adapter.updateList(tracks.toMutableList())
            updateUIVisibility(tracks.isNotEmpty())
        }

        viewModel.searchHistory.observe(this) { history ->
            historyAdapter.updateList(history.toMutableList())
            binding.searchHistoryLayout.visibility = if (history.isNotEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBarLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.NotConnectedLayout.visibility = View.VISIBLE
            } else {
                binding.NotConnectedLayout.visibility = View.GONE
            }
        }
    }

    private fun updateUIVisibility(hasResults: Boolean) {
        binding.trackRecycler.visibility = if (hasResults) View.VISIBLE else View.GONE
        binding.NotFoundLayout.visibility = if (!hasResults) View.VISIBLE else View.GONE
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun openAudioPlayer(track: Track) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(CURRENT_TRACK, Gson().toJson(track))
        startActivity(intent)
    }
}