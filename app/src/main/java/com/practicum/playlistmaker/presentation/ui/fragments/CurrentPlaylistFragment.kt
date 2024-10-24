package com.practicum.playlistmaker.presentation.ui.fragments

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCurrentPlaylistBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.ui.activity.MainActivity
import com.practicum.playlistmaker.presentation.ui.adapter.TrackAdapter
import com.practicum.playlistmaker.presentation.viewmodel.CurrentPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class CurrentPlaylistFragment : Fragment() {
    private var _binding: FragmentCurrentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModel<CurrentPlaylistViewModel>()
    private lateinit var playlist: Playlist
    private lateinit var callback: OnBackPressedCallback
    private val adapter = TrackAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = (arguments?.getParcelable("selected_playlist") as? Playlist)!!

        adapter.itemClickListener = {
            openAudioPlayer(it)
        }
        adapter.itemLongClickListener = {
            showDialog(playlist, it.trackId?.toLong() ?: 0)
        }

        (activity as? MainActivity)?.hideNavBar()

        binding.TracksRV.adapter = adapter

        renderCurrentPlaylist()

        val playlistId = playlist.id
        val screenHeight = resources.displayMetrics.heightPixels
        val allowableHeight = (screenHeight * 0.25).toInt()
        val bottomSheetBehavior =
            BottomSheetBehavior.from(binding.BottomMenuLayout).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        val bottomSheetBehaviorPlaylist =
            BottomSheetBehavior.from(binding.BottomMenuLayout).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        bottomSheetBehaviorPlaylist.peekHeight = allowableHeight

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bottomSheetBehaviorPlaylist.isHideable = false
                        binding.BottomSheetOverlay.visibility = View.GONE
                    }

                    else -> {
                        bottomSheetBehaviorPlaylist.isHideable = true
                        bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_HIDDEN
                        binding.BottomSheetOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        })
        val bundle = Bundle().apply {
            putParcelable("modify_playlist", playlist)
        }
        binding.CurrentPlaylistEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_currentPlaylistFragment_to_modifyPlaylistFragment,
                bundle
            )
        }

        binding.Toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.ShareImage.setOnClickListener {
            toShare()
        }

        binding.CurrentPlaylistShare.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            toShare()
        }

        binding.CurrentPlaylistDeletae.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            toDelete()
        }

        binding.MenuImage.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.CurrentPlaylistTitle.text = playlist.name
            binding.CurrentPlaylistTracksCount.text = countTracks(playlist.tracksAmount)
        }

        vm.getPlaylistById(playlistId)

        vm.observeTrackCount().observe(viewLifecycleOwner) { trackCount ->
            binding.CurrentPlaylistTracksCount.text = countTracks(trackCount)
        }

        vm.observePlaylistAllTime().observe(viewLifecycleOwner) {
            vm.playlistAllTime()
            if (it != null) {
                renderDuration(it)
            }
        }

        vm.observePlaylistId().observe(viewLifecycleOwner) {
            playlist = it
            vm.playlistAllTime()
            vm.getAllTracks(playlist.tracksIds)
        }

        vm.observePlaylistTracks().observe(viewLifecycleOwner) {
            if (it != null) {
                showContent(it)
                vm.playlistAllTime()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (playlist.tracksAmount == 0) {
            binding.NoTracksText.visibility = View.VISIBLE
        } else {
            binding.NoTracksText.visibility = View.GONE
        }

        val playlistId = playlist.id
        vm.getPlaylistById(playlistId)
        vm.observePlaylistId().observe(viewLifecycleOwner) {
            playlist = it
            vm.getAllTracks(playlist.tracksIds)
            vm.playlistAllTime()
            renderCurrentPlaylist()
        }
    }

    private fun renderDuration(time: Long) {
        val duration = SimpleDateFormat("mm", Locale.getDefault()).format(time).toInt()
        val formattedDuration =
            resources.getQuantityString(R.plurals.minutes, duration, duration)
        binding.AllTime.text = formattedDuration
    }

    private fun toDelete() {
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogStyle)
            .setMessage(getString(R.string.AcceptDeletePlaylist))
            .setNegativeButton(getString(R.string.No)) { dialog, witch -> }
            .setPositiveButton(getString(R.string.Yes)) { dialog, witch ->
                vm.deletePlaylist()
                findNavController().navigateUp()
            }.show()
    }

    private fun toShare() {
        val tracks = vm.observePlaylistTracks().value ?: emptyList()
        if (tracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.NoTracksToShare),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val message = formMessage()
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }

    private fun formMessage(): String {
        val name = playlist.name
        val descript = playlist.description
        val tracks = vm.observePlaylistTracks().value ?: emptyList()
        val stringBuilder = StringBuilder()
        with(stringBuilder) {
            append("$name\n")
            append("$descript\n")
            append("${countTracks(tracks.size)}\n")
        }
        for ((index, track) in tracks.withIndex()) {
            stringBuilder.append(
                "${index + 1}. ${track.artistName} - ${track.trackName} (${
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(track.trackTimeMillis)
                })\n"
            )
        }
        return stringBuilder.toString()
    }

    private fun renderCurrentPlaylist() {
        binding.TrackCount.text = countTracks(playlist.tracksAmount)
        binding.PlaylistTitle.text = playlist.name
        binding.PlaylistDescription.text = playlist.description

        val imageUri = playlist.imageUri.let { Uri.parse(it) }
        Glide.with(requireContext())
            .load(imageUri)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.PlaylistImage)

        Glide.with(requireContext())
            .load(imageUri)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.CurrentPlaylistCover)
    }

    private fun countTracks(count: Int): String {
        val countTrack = context?.resources?.getQuantityString(R.plurals.track_count, count)
        return "$count $countTrack"

    }

    private fun showContent(tracks: List<Track>) {
        binding.TracksRV.visibility = View.VISIBLE
        adapter.updateList(tracks.toMutableList())
    }

    private fun openAudioPlayer(track: Track) {
        val bundle = Bundle()
        bundle.putParcelable("SEARCH_QUERY_HISTORY", track)
        findNavController().navigate(
            R.id.action_currentPlaylistFragment_to_audioPlayerFragment,
            bundle
        )
    }

    private fun showDialog(playlist: Playlist, trackId: Long) {
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogStyle)
            .setMessage(getString(R.string.AcceptDeleteTrack))
            .setNegativeButton(getString(R.string.No)) { dialog, witch -> }
            .setPositiveButton(getString(R.string.Yes)) { dialog, witch ->
                vm.deleteTrackFromPlaylist(playlist, trackId)
                binding.CurrentPlaylistTracksCount.text =
                    countTracks(vm.observePlaylistTracks().value?.size ?: 0)
                val position = adapter.trackList.indexOfFirst { it.trackId?.toLong() == trackId }
                if (position != -1) {
                    adapter.trackList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (::callback.isInitialized) {
            callback.remove()
        }
    }
}