package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SearchHistoryRepository {

    override fun getSearchHistory(): List<Track> {
        val json = sharedPreferences.getString(PREF_KEY, null)
        return if (json != null) {
            gson.fromJson(json, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

    override fun addTrackToHistory(track: Track) {
        val history = getSearchHistory().toMutableList()
        history.removeIf { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > HISTORY_MAX_SIZE) {
            history.removeAt(history.lastIndex)
        }
        val json = gson.toJson(history)
        sharedPreferences.edit().putString(PREF_KEY, json).apply()
    }

    override fun clearSearchHistory() {
        sharedPreferences.edit().remove(PREF_KEY).apply()
    }

    companion object {
        private const val PREF_KEY = "search_history"
        private const val HISTORY_MAX_SIZE = 10
    }
}