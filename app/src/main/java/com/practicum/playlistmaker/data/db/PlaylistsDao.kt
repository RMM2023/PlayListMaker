package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(track: TrackToPlaylistEntity)

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlayLists(): List<PlaylistEntity>

    @Update
    suspend fun updatePlayList(playList: PlaylistEntity)

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity

    @Query("UPDATE playlists SET tracksAmount = tracksAmount - 1 WHERE id = :playlistId")
    suspend fun decrementPlaylistTrackCount(playlistId: Int)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_tracks")
    fun getAllPlaylistTracks(): List<TrackToPlaylistEntity>

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)
}