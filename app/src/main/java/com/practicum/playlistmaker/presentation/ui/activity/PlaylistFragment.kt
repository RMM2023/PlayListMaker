package com.practicum.playlistmaker.presentation.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding

class PlaylistFragment : Fragment() {
    var _binding : FragmentPlaylistBinding? = null
    val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding?.root
    }

    companion object {
        fun newInstance() = PlaylistFragment()

    }
}