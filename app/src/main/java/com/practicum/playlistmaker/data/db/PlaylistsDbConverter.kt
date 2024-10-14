package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.domain.model.Playlist

class PlaylistsDbConverter {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverPath,
            playlist.tracksIds,
            playlist.tracksAmount,
            playlist.imageUri
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverPath,
            playlist.tracksIds,
            playlist.tracksAmount,
            playlist.imageUri
        )
    }
}