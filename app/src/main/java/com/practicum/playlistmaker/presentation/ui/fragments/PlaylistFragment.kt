package com.practicum.playlistmaker.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.PlaylistState
import com.practicum.playlistmaker.presentation.ui.activity.MainActivity
import com.practicum.playlistmaker.presentation.ui.adapter.PlaylistsAdapter
import com.practicum.playlistmaker.presentation.ui.adapter.PlaylistsViewHolder
import com.practicum.playlistmaker.presentation.viewmodel.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(), PlaylistsViewHolder.ClickListener {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlaylistsAdapter

    private val viewModel by viewModel<PlayListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noPlaylistImage.visibility = View.GONE
        binding.noPlaylistText.visibility = View.GONE

        viewModel.getPlaylists()


        adapter = PlaylistsAdapter(this)
        binding.playlistsRV.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsRV.adapter = adapter

        viewModel.playlistState.observe(viewLifecycleOwner) {
            execute(it)
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.mediaToNewPlaylist)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun execute(state: PlaylistState) {
        when (state) {
            is PlaylistState.Data -> {
                val playlists = state.tracks
                showData()
                adapter.playlists = playlists as ArrayList<Playlist>
                adapter.notifyDataSetChanged()
                (activity as? MainActivity)?.showNavBar()
            }

            is PlaylistState.Empty -> {
                showEmpty()
            }

            is PlaylistState.Load -> {
                binding.playlistsProgressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun showData() {
        binding.playlistsProgressBar.visibility = View.GONE
        binding.noPlaylistImage.visibility = View.GONE
        binding.noPlaylistText.visibility = View.GONE
        binding.playlistsRV.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.playlistsProgressBar.visibility = View.GONE
        binding.noPlaylistImage.visibility = View.VISIBLE
        binding.noPlaylistText.visibility = View.VISIBLE
        binding.playlistsRV.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onClick(playlist: Playlist) {
        // тут будет реализовано нажатие в будущем
    }
}