package com.practicum.playlistmaker.domain.model

sealed interface PlaylistState {
    data class Data(val tracks: List<Playlist>) : PlaylistState

    data object Empty : PlaylistState

    data object Load : PlaylistState
}