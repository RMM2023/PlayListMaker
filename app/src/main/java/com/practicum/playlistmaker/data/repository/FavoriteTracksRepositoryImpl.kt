package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.db.AppDataBase
import com.practicum.playlistmaker.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoriteTracksRepositoryImpl(val appDataBase: AppDataBase) : FavoriteTracksRepository {
    override suspend fun insertTrack(track : Track){
        GlobalScope.launch {
            appDataBase.favoriteTracksDao().insertTrack(track.toFavoriteTrackEntity())
        }
    }
    override suspend fun deleteTracK(track: Track){
        GlobalScope.launch {
            appDataBase.favoriteTracksDao().deleteTracK(track.toFavoriteTrackEntity())
        }
    }
    override fun getFavoriteTracks() : Flow<List<Track>>{
        return appDataBase.favoriteTracksDao().getFavoriteTracks().map { list ->
            list.map { it.toTrack() }
        }
    }
    override fun getFavoriteTracksIds() : Flow<List<String>>{
        return appDataBase.favoriteTracksDao().getFavoriteTracksIds()
    }

    fun Track.toFavoriteTrackEntity() : FavoriteTrackEntity{
        return FavoriteTrackEntity(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            collectionName = collectionName,
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName,
            country = country,
            previewUrl = previewUrl
        )
    }

    fun FavoriteTrackEntity.toTrack() : Track{
        return Track(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            collectionName = collectionName,
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName,
            country = country,
            previewUrl = previewUrl,
            isFavorite = true
        )
    }
}