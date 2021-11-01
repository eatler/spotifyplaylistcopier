package com.example.spotifysdktest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotifysdktest.data.models.Playlist
import com.example.spotifysdktest.data.repository.ApiCalls

class SearchFragmentViewModel(private val apiCalls: ApiCalls) : ViewModel() {
    private val playlists: MutableLiveData<MutableList<Playlist>> by lazy {
        MutableLiveData<MutableList<Playlist>>()
    }

    private val currentQuery: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getCurrentQuery(): LiveData<String> {
        return currentQuery
    }

    fun getPlaylists(): LiveData<MutableList<Playlist>> {
        return playlists
    }

    fun searchPlaylists(query: String?) {
        currentQuery.value = query
        if (query != null && query.isNotEmpty()) {
            apiCalls.getSearchPlaylists(query).get {
                playlists.value = it
            }
        } else {
            playlists.value = mutableListOf()
        }
    }


    class SearchFragmentViewModelFactory(private val apiCalls: ApiCalls) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchFragmentViewModel::class.java)) {
                return SearchFragmentViewModel(apiCalls) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}