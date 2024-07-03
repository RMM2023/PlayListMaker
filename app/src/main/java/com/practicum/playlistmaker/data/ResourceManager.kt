package com.practicum.playlistmaker.data

import android.content.Context
import androidx.annotation.StringRes

class ResourceManager(private val context: Context) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
}