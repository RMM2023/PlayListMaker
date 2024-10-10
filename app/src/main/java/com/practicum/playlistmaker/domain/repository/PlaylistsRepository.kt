package com.practicum.playlistmaker.domain.repository
import android.content.Context
import android.net.Uri
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playList: Playlist, track: Track)
    suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri?
    suspend fun getPlaylistById(playlistId: Int): Playlist
    suspend fun deletePlaylist(playlistId: Int)
    suspend fun newPlaylist(playlistName: String, playlistDescription: String, coverUri: Uri?)
    suspend fun getCover(): String
}