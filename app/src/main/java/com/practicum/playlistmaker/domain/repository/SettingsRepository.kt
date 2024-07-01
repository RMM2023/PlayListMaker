package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun setThemeSettings(settings: ThemeSettings)
    fun getShareAppLink(): String
    fun getSupportEmailData(): Triple<String, String, String>
    fun getUserAgreementLink(): String
}