package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface TrackRepository {
    fun getTrack(json: String?): Track?
}