package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteTrackEntity::class, PlaylistEntity::class, TrackToPlaylistEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao() : FavoriteTracksDao
    abstract fun playlistsDao() : PlaylistsDao
}