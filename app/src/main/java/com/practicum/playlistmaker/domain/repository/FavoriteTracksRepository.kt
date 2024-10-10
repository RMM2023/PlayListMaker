package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    suspend fun insertTrack(track : Track)
    suspend fun deleteTracK(track: Track)
    fun getFavoriteTracks() : Flow<List<Track>>
    fun getFavoriteTracksIds() : Flow<List<String>>
}