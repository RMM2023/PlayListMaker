package com.practicum.playlistmaker.data.repository

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TrackRepository

class TrackRepositoryImpl : TrackRepository {
    override fun getTrack(json: String?): Track? {
        return try {
            Gson().fromJson(json, Track::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }
}