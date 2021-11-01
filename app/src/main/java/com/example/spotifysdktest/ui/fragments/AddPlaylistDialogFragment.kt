package com.example.spotifysdktest.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.spotifysdktest.R
import com.example.spotifysdktest.data.repository.ApiCalls
import org.jetbrains.anko.find

class AddPlaylistDialogFragment : DialogFragment() {
    private var onAddUpdate: () -> Unit = {}

    fun setOnDismissListener(onAddUpdate: () -> Unit) {
        this.onAddUpdate = onAddUpdate
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val copiedPlaylistName = arguments?.getString("playlistName")
        val copiedPlaylistId = arguments?.getString("playlistId")
        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity)

            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.dialog_add_playlist, null))
                .setTitle(R.string.copy_playlist)
                .setPositiveButton(
                    R.string.add
                ) { dialog, id ->
                    val apiCalls = ApiCalls(fragmentActivity)
                    val playlistName =
                        getDialog()?.find<EditText>(R.id.playlist_name_edittext)?.text
                    val name =
                        if (playlistName.isNullOrEmpty()) copiedPlaylistName else playlistName
                    apiCalls.postCreatePlaylist(name.toString()).post { playlistId ->
                        if (copiedPlaylistId != null) {
                            apiCalls.getTracksPlaylist(copiedPlaylistId).get { tracks ->
                                apiCalls.postAddItemsPlaylist(playlistId, tracks).post {
                                    Toast.makeText(
                                        fragmentActivity,
                                        "$name added",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onAddUpdate()
                                }
                            }
                        }
                    }
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, id ->

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}