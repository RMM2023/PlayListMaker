package com.practicum.playlistmaker.presentation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleTheme(isChecked)
            (requireContext().applicationContext as App).themeToggle(isChecked)
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, viewModel.getShareAppLink())
            }
            startActivity(Intent.createChooser(shareIntent, "Share app"))
        }

        binding.supportButton.setOnClickListener {
            val (email, subject, body) = viewModel.getSupportEmailData()
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
            startActivity(supportIntent)
        }

        binding.agreeButton.setOnClickListener {
            val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.getUserAgreementLink()))
            startActivity(agreeIntent)
        }
    }

    private fun observeViewModel() {
        viewModel.themeSettings.observe(viewLifecycleOwner) {settings ->
            binding.themeSwitch.isChecked = settings.isDarkTheme
        }
    }
}