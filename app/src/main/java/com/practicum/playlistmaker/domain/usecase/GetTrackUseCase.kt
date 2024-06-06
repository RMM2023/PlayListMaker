package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TrackRepository

class GetTrackUseCase(private val trackRepository: TrackRepository) {
    fun execute(json: String?): Track? {
        return trackRepository.getTrack(json)
    }
}