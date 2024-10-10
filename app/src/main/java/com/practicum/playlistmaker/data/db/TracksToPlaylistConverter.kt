package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.domain.model.Track

class TracksToPlaylistConverter {
    fun map(track: Track, addTime: Long): TrackToPlaylistEntity {
        return TrackToPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            addTime
        )
    }

    fun map(track: TrackToPlaylistEntity, addTime: Long): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            false,
            addTime
        )
    }
}