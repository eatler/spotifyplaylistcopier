package com.example.spotifysdktest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotifysdktest.data.models.Track
import com.example.spotifysdktest.data.repository.ApiCalls

class TrackActivityViewModel(private val apiCalls: ApiCalls, private val playlistId: String?) :
    ViewModel() {
    private val tracks: MutableLiveData<MutableList<Track>> by lazy {
        MutableLiveData<MutableList<Track>>().also {
            loadTracks()
        }

    }

    fun getTracks(): LiveData<MutableList<Track>> {
        return tracks
    }

    private fun loadTracks() {
        if (playlistId != null) {
            apiCalls.getTracksPlaylist(playlistId).get {
                tracks.value = it
            }
        }
    }

    class TrackActivityViewModelFactory(
        private val apiCalls: ApiCalls,
        private val playlistId: String?
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TrackActivityViewModel::class.java)) {
                return TrackActivityViewModel(apiCalls, playlistId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}