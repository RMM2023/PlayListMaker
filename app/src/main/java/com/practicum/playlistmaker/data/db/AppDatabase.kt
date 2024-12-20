package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteTrackEntity::class], version = 2)

abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao() : FavoriteTracksDao
}