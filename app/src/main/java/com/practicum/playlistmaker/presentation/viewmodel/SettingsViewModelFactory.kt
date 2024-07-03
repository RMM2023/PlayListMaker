package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.usecase.*

class SettingsViewModelFactory(
    private val getThemeSettingsUseCase: GetThemeSettingsUseCase,
    private val setThemeSettingsUseCase: SetThemeSettingsUseCase,
    private val getShareAppLinkUseCase: GetShareAppLinkUseCase,
    private val getSupportEmailDataUseCase: GetSupportEmailDataUseCase,
    private val getUserAgreementLinkUseCase: GetUserAgreementLinkUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(
                getThemeSettingsUseCase,
                setThemeSettingsUseCase,
                getShareAppLinkUseCase,
                getSupportEmailDataUseCase,
                getUserAgreementLinkUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}