package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [PlaylistEntity::class, TrackToPlaylistEntity::class], version = 3)
@TypeConverters(TracksIdsConverter::class)

abstract class PlaylistsDatabase : RoomDatabase() {
    abstract fun playlistsDao(): PlaylistsDao
}