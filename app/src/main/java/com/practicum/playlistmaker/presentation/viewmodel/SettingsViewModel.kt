package com.practicum.playlistmaker.presentation.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.model.ThemeSettings
import com.practicum.playlistmaker.domain.usecase.*

class SettingsViewModel(
    private val getThemeSettingsUseCase: GetThemeSettingsUseCase,
    private val setThemeSettingsUseCase: SetThemeSettingsUseCase,
    private val getShareAppLinkUseCase: GetShareAppLinkUseCase,
    private val getSupportEmailDataUseCase: GetSupportEmailDataUseCase,
    private val getUserAgreementLinkUseCase: GetUserAgreementLinkUseCase
) : ViewModel() {

    private val _themeSettings = MutableLiveData<ThemeSettings>()
    val themeSettings: LiveData<ThemeSettings> = _themeSettings

    init {
        _themeSettings.value = getThemeSettingsUseCase.execute()
    }

    fun toggleTheme(isDarkTheme: Boolean) {
        val newSettings = ThemeSettings(isDarkTheme)
        setThemeSettingsUseCase.execute(newSettings)
        _themeSettings.value = newSettings
    }

    fun getShareAppLink(): String = getShareAppLinkUseCase.execute()

    fun getSupportEmailData(): Triple<String, String, String> = getSupportEmailDataUseCase.execute()

    fun getUserAgreementLink(): String = getUserAgreementLinkUseCase.execute()

    fun applyTheme(isDarkTheme: Boolean) {
        // Здесь логика применения темы
        // Например, можно использовать AppCompatDelegate
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}