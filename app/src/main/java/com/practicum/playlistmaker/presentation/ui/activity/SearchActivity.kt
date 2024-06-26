package com.practicum.playlistmaker.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.CURRENT_TRACK
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.presentation.util.SearchHistoryHelper
import com.practicum.playlistmaker.presentation.ui.adapter.TrackAdapter

class SearchActivity : AppCompatActivity() {
    companion object {
        const val PREF_NAME = "pref_name"
    }

    private lateinit var binding: ActivitySearchBinding
    private var sharedPreferencesHistory: SharedPreferences? = null
    private lateinit var searchHistoryClass: SearchHistoryHelper
    private var trackList = mutableListOf<Track>()
    private var historyTrackList = mutableListOf<Track>()
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private lateinit var searchTracksUseCase: SearchTracksUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchTracksUseCase = Creator.provideSearchTracksUseCase()

        adapter = TrackAdapter(trackList) { track ->
            searchHistoryClass.add(track)
            historyTrackList.add(track)
            historyAdapter.updateList(historyTrackList)
            openAudioPlayer(track)
        }
        binding.trackRecycler.adapter = adapter
        binding.trackRecycler.layoutManager = LinearLayoutManager(this)

        binding.historyClearButton.setOnClickListener {
            searchHistoryClass.clear()
            historyTrackList.clear()
            historyAdapter.updateList(historyTrackList)
            searchHistoryLayoutVis(false)
            trackRecyclerViewVis(true)
        }

        sharedPreferencesHistory = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        sharedPreferencesHistory?.let {
            searchHistoryClass = SearchHistoryHelper(it)
        }

        historyAdapter = TrackAdapter(historyTrackList) { track ->
            openAudioPlayer(track)
        }
        binding.searchHistoryRecycleView.adapter = historyAdapter
        binding.searchHistoryRecycleView.layoutManager = LinearLayoutManager(this)
        readHistory()
        if (historyAdapter.trackList.isNotEmpty()) {
            searchHistoryLayoutVis(true)
            trackRecyclerViewVis(false)
        } else {
            trackRecyclerViewVis(true)
        }

        binding.updateButton.setOnClickListener {
            onUpdate()
        }
        binding.searchClearButton.setOnClickListener {
            binding.searchEditText.setText("")
            hideKeyboard()
            binding.searchEditText.clearFocus()
            adapter.clearList()
            notConnectedLayoutVis(false)
            notFoundLayoutVis(false)
            readHistory()
            if (historyTrackList.isEmpty()) {
                searchHistoryLayoutVis(false)
            } else {
                searchHistoryLayoutVis(true)
            }
        }

        binding.searchBackButton.setOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchRunnable?.let { handler.removeCallbacks(it) }
                if (s.toString().isEmpty()) {
                    binding.searchClearButton.visibility = View.GONE
                    trackRecyclerViewVis(false)
                    notConnectedLayoutVis(false)
                    notFoundLayoutVis(false)
                    readHistory()
                    if (historyTrackList.isEmpty()) {
                        searchHistoryLayoutVis(false)
                    } else {
                        searchHistoryLayoutVis(true)
                    }
                    hideKeyboard()
                } else {
                    binding.searchClearButton.visibility = View.VISIBLE
                    searchRunnable = Runnable {
                        searchHistoryLayoutVis(false)
                        trackRecyclerViewVis(true)
                        onSearch(s.toString())
                    }
                    handler.postDelayed(searchRunnable!!, 2000)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.searchEditText.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SAVE_TEXT", binding.searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val saveText = savedInstanceState.getString("SAVE_TEXT", "")
        binding.searchEditText.setText(saveText)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun notFoundLayoutVis(vis: Boolean) {
        binding.NotFoundLayout.visibility = if (vis) View.VISIBLE else View.GONE
    }

    private fun notConnectedLayoutVis(vis: Boolean) {
        binding.NotConnectedLayout.visibility = if (vis) View.VISIBLE else View.GONE
    }

    private fun searchHistoryLayoutVis(vis: Boolean) {
        binding.searchHistoryLayout.visibility = if (vis) View.VISIBLE else View.GONE
    }

    private fun trackRecyclerViewVis(vis: Boolean) {
        binding.trackRecycler.visibility = if (vis) View.VISIBLE else View.GONE
    }

    private fun readHistory() {
        historyTrackList = searchHistoryClass.read().toMutableList()
        historyAdapter.updateList(historyTrackList)
    }

    private fun onUpdate() {
        notFoundLayoutVis(false)
        notConnectedLayoutVis(false)
        onSearch(binding.searchEditText.text.toString())
    }

    private fun onSearch(query: String) {
        binding.progressBarLayout.visibility = View.VISIBLE
        searchTracksUseCase.execute(query) { result ->
            binding.progressBarLayout.visibility = View.GONE
            result.fold(onSuccess = { searchResult ->
                if (searchResult.resultCount > 0) {
                    trackList = searchResult.results.toMutableList()
                    adapter.updateList(trackList)
                    binding.trackRecycler.scrollToPosition(0)
                } else {
                    adapter.clearList()
                    notConnectedLayoutVis(false)
                    notFoundLayoutVis(true)
                }
            }, onFailure = {
                adapter.clearList()
                notConnectedLayoutVis(true)
                notFoundLayoutVis(false)
            })
        }
    }

    private fun openAudioPlayer(track: Track) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(CURRENT_TRACK, Gson().toJson(track))
        startActivity(intent)
    }
}
