package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.THEME_KEY
import com.practicum.playlistmaker.data.ResourceManager
import com.practicum.playlistmaker.domain.model.ThemeSettings
import com.practicum.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val resourceManager: ResourceManager
) : SettingsRepository {


    override fun getThemeSettings(): ThemeSettings {
        val isDarkTheme = sharedPreferences.getBoolean(THEME_KEY, false)
        return ThemeSettings(isDarkTheme)
    }

    override fun setThemeSettings(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(THEME_KEY, settings.isDarkTheme).apply()
    }

    override fun getShareAppLink(): String {
        return resourceManager.getString(R.string.course_url)
    }

    override fun getSupportEmailData(): Triple<String, String, String> {
        return Triple(
            resourceManager.getString(R.string.my_Email),
            resourceManager.getString(R.string.support_subject),
            resourceManager.getString(R.string.support_text)
        )
    }

    override fun getUserAgreementLink(): String {
        return resourceManager.getString(R.string.agreement_uri)
    }
}