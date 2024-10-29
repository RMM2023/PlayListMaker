package com.practicum.playlistmaker.presentation.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.presentation.ui.activity.MainActivity
import com.practicum.playlistmaker.presentation.viewmodel.ModifyPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ModifyPlaylistFragment : NewPlaylistFragment() {
    private val vm by viewModel<ModifyPlaylistViewModel>()
    private var playlist: Playlist? = null
    private var coverUriSelect: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.hideNavBar()

        playlist = arguments?.getParcelable("modify_playlist") as? Playlist

        val chooseCover =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { previewUri ->
                if (previewUri != null) {
                    vm.saveCoverToStorage(previewUri, requireContext())
                    binding.newPlaylistCover.setImageURI(previewUri)
                }
            }

        binding.toolbarTitle.text = getString(R.string.edit)

        binding.createButton.text = getString(R.string.save)

        binding.newPlaylistCover.setOnClickListener {
            chooseCover.launch(PickVisualMediaRequest((ActivityResultContracts.PickVisualMedia.ImageOnly)))
        }

        binding.topPanel.setOnClickListener() {
            findNavController().navigateUp()
        }

        vm.savedCoverUri.observe(viewLifecycleOwner, Observer { uri ->
            coverUriSelect = uri
        })

        vm.playlist.observe(viewLifecycleOwner) { playlist ->
            vm.getPlaylist(playlist)
        }

        binding.createButton.setOnClickListener {
            playlist.let { playlist ->
                modifyPlaylist(coverUriSelect, playlist!!)
                findNavController().popBackStack()
            }
        }

        playlist.let { playlist ->
            binding.newPlaylistNameEditTxt.setText(playlist?.name)
            binding.newPlaylistDescriptionEditTxt.setText(playlist?.description)
            if (!playlist?.imageUri.isNullOrEmpty()) {
                val imageUri = Uri.parse(playlist?.imageUri)
                coverUriSelect = imageUri
                binding.newPlaylistCover.setImageURI(imageUri)
            }
        }
    }

    private fun modifyPlaylist(coverUri: Uri?, originalPlayList: Playlist) {
        val name = binding.newPlaylistNameEditTxt.text.toString()
        val description = binding.newPlaylistDescriptionEditTxt.text.toString()
        val cover = vm.getCover().toString()
        vm.modifyData(name, description, cover, coverUri, originalPlayList)
    }
}