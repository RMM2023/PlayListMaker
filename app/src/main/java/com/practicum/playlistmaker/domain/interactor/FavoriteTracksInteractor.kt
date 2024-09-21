package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractor(val favoriteTracksRepository: FavoriteTracksRepository) {
    suspend fun insertTrack(track : Track){
        favoriteTracksRepository.insertTrack(track)
    }
    suspend fun deleteTracK(track: Track){
        favoriteTracksRepository.deleteTracK(track)
    }
    fun getFavoriteTracks() : Flow<List<Track>>{
        return favoriteTracksRepository.getFavoriteTracks()
    }
    fun getFavoriteTracksIds() : Flow<List<String>>{
        return favoriteTracksRepository.getFavoriteTracksIds()
    }
}