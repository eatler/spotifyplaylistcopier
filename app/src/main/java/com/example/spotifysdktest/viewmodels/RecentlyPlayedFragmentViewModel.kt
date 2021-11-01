package com.example.spotifysdktest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotifysdktest.data.models.Playlist
import com.example.spotifysdktest.data.repository.ApiCalls

class RecentlyPlayedFragmentViewModel(private val apiCalls: ApiCalls) : ViewModel() {
    private val playlists: MutableLiveData<MutableList<Playlist>> by lazy {
        MutableLiveData<MutableList<Playlist>>().also {
            it.value = mutableListOf()
            loadRecentPlaylists()
        }
    }

    private val isRefreshing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also {
            it.value = false
        }
    }

    fun getPlaylists(): LiveData<MutableList<Playlist>> {
        return playlists
    }

    fun getIsRefreshing(): LiveData<Boolean> {
        return isRefreshing
    }

    fun loadRecentPlaylists() {
        apiCalls.getRecentPlaylistsIds().get({
            isRefreshing.value = false
        }, {
            playlists.value = it
            isRefreshing.value = false
        })
    }

    class RecentlyPlayedFragmentViewModelFactory(private val apiCalls: ApiCalls) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecentlyPlayedFragmentViewModel::class.java)) {
                return RecentlyPlayedFragmentViewModel(apiCalls) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}