package com.practicum.playlistmaker.data

import android.content.SharedPreferences
import com.google.gson.Gson


const val PREF_KEY = "history"
const val HISTORY_MAX_SIZE = 10
class SearchHistoryHelper(
    var sharedPref: SharedPreferences) {

    fun read(): List<Track> {
        val json = sharedPref.getString(PREF_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    fun clear() = sharedPref
        .edit()
        .clear()
        .apply()

    fun add(newTrack: Track) {
        var tempList = read().toMutableList()
        tempList.removeIf { it.trackId == newTrack.trackId }
        tempList.add(0, newTrack)
        if (tempList.size > HISTORY_MAX_SIZE) {
            tempList = tempList.subList(0, HISTORY_MAX_SIZE)
        }
        val json = Gson().toJson(tempList)
        sharedPref.edit()
            .putString(PREF_KEY, json)
            .apply()
    }
}