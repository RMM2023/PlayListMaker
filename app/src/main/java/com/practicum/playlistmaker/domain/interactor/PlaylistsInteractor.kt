package com.practicum.playlistmaker.domain.interactor

import android.content.Context
import android.net.Uri
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractor(private val playlistsRepository: PlaylistsRepository) {
    suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistsRepository.addTrackToPlaylist(playlist, track)
    }

    suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri? {
        return playlistsRepository.saveCoverToPrivateStorage(previewUri, context)
    }

    suspend fun getPlaylistById(playlistId: Int): Playlist {
        return playlistsRepository.getPlaylistById(playlistId)
    }

    suspend fun deletePlaylist(playlistId: Int) {
        return playlistsRepository.deletePlaylist(playlistId)
    }

    suspend fun newPlaylist(
        playlistName: String,
        playlistDescription: String,
        coverUri: Uri?
    ) {
        return playlistsRepository.newPlaylist(playlistName, playlistDescription, coverUri)
    }

    suspend fun getCover(): String {
        return playlistsRepository.getCover()
    }

    suspend fun getAllTracks(tracksIds: List<Long>): List<Track>{
        return playlistsRepository.getAllTracks(tracksIds)
    }

    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Long){
        return playlistsRepository.deleteTrackFromPlaylist(playlistId,trackId)
    }

    suspend fun trackCountDecrement(playlistId: Int){
        return playlistsRepository.trackCountDecrement(playlistId)
    }

    suspend fun modifyData(name: String, description: String,cover: String, coverUri: Uri?,originalPlayList: Playlist) {
        playlistsRepository.modifyData(name, description, cover, coverUri, originalPlayList)
    }

}