package com.practicum.playlistmaker.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.data.db.PlaylistsDbConverter
import com.practicum.playlistmaker.data.db.TracksToPlaylistConverter
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.UUID


class PlaylistsRepositoryImpl(
    private val playlistsDatabase: PlaylistsDatabase,
    private val playlistsDbConverter: PlaylistsDbConverter,
    private val tracksToPlaylistConverter: TracksToPlaylistConverter
) : PlaylistsRepository {
    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistsDatabase.playlistsDao().getAllPlayLists()
        emit(converterForEntity(playlists))
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsDatabase.playlistsDao().insertPlayList(playlistsDbConverter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playList: Playlist, track: Track) {
        val trackId = track.trackId?.toLong()

        if (trackId == 0L) {
            throw IllegalArgumentException("TrackID is 0")
        }
        addPlaylist(playlist = playList)
        val addTime = Date().time
        playlistsDatabase.playlistsDao()
            .addTrackToPlaylist(tracksToPlaylistConverter.map(track, addTime))
    }

    override suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri? {
        val path =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlistCovers")
        if (!path.exists()) {
            path.mkdirs()
        }
        val file = File(path, "playlist_cover_${System.currentTimeMillis()}.jpg")
        val input = context.contentResolver.openInputStream(previewUri)
        val output = FileOutputStream(file)
        BitmapFactory
            .decodeStream(input)
            .compress(Bitmap.CompressFormat.JPEG, 30, output)
        val previewUri = Uri.fromFile(file)
        return previewUri
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return converterForPlaylistEntity(
            playlistsDatabase.playlistsDao().getPlaylistById(playlistId)
        )
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val playlist = getPlaylistById(playlistId)
        playlistsDatabase.playlistsDao().deletePlaylist(playlistsDbConverter.map(playlist))
    }

    override suspend fun newPlaylist(
        playlistName: String,
        playlistDescription: String,
        coverUri: Uri?
    ) {
        val coverPath = getCover()
        val playlist = Playlist(
            id = 0,
            name = playlistName,
            description = playlistDescription,
            coverPath = coverPath,
            tracksIds = arrayListOf(),
            tracksAmount = 0,
            imageUri = coverUri?.toString() ?: ""
        )
        addPlaylist(playlist)
    }

    override suspend fun getCover(): String {
        return "cover_${UUID.randomUUID()}.jpg"
    }

    private fun converterForEntity(playlist: List<PlaylistEntity>): List<Playlist> {
        return playlist.map { playlist -> playlistsDbConverter.map(playlist) }
    }

    private fun converterForPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return playlistsDbConverter.map(playlist)
    }
}