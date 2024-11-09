package com.practicum.playlistmaker.domain.repository
import android.content.Context
import android.net.Uri
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri?
    suspend fun getPlaylistById(playlistId: Int): Playlist
    suspend fun deletePlaylist(playlistId: Int)
    suspend fun newPlaylist(playlistName: String, playlistDescription: String, coverUri: Uri?)
    suspend fun getCover(): String
    suspend fun getAllTracks(tracksIds: List<Long>): List<Track>
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Long)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun trackCountDecrement(playlistId: Int)
    suspend fun modifyData(name: String, description: String, cover: String, coverUri: Uri?, originalPlayList: Playlist)
}