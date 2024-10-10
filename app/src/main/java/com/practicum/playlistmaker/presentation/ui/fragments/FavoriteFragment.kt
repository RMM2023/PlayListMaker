package com.practicum.playlistmaker.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.practicum.playlistmaker.CURRENT_TRACK
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.presentation.ui.activity.AudioPlayerActivity
import com.practicum.playlistmaker.presentation.ui.adapter.TrackAdapter
import com.practicum.playlistmaker.presentation.viewmodel.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    var _binding : FragmentFavoriteBinding? = null
    val binding get() = _binding
    val viewModel by viewModel<FavoriteViewModel>()
    lateinit var adapter : TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TrackAdapter(mutableListOf()){track ->
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
            intent.putExtra(CURRENT_TRACK, Gson().toJson(track))
            startActivity(intent)
        }
        binding!!.recyclerviewFavorites.adapter = adapter

        viewModel.favoriteTracks.observe(viewLifecycleOwner){tracks ->
            if (tracks.isEmpty()) {
                binding!!.emptyStateLayout.visibility = View.VISIBLE
                binding!!.recyclerviewFavorites.visibility = View.GONE
            } else {
                binding!!.emptyStateLayout.visibility = View.GONE
                binding!!.recyclerviewFavorites.visibility = View.VISIBLE
                adapter.updateList(tracks.toMutableList())
            }
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()

    }
}