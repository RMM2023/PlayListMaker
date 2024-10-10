package com.practicum.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val coverPath: String,
    val tracksIds: ArrayList<Long>,
    val tracksAmount: Int,
    val imageUri: String?
)