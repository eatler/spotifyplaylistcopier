package com.example.spotifysdktest.data.repository

import android.content.Context
import com.example.spotifysdktest.data.models.Track

class ApiCalls(val context: Context) {
    fun getRecentPlaylists(playlistId: String) =
        PlaylistById(context = context, playlistId)

    fun getRecentPlaylistsIds() = RecentPlaylists(context = context)
    fun getUsersPlaylists() = UsersPlaylists(context = context)
    fun getSearchPlaylists(query: String) = SearchPlaylists(context = context, query)
    fun getTracksPlaylist(playlistId: String) = TracksPlaylist(context, playlistId)
    fun getCurrentUserId() = CurrentUserId(context)
    fun postCreatePlaylist(name: String) = CreatePlaylist(context, name)
    fun postAddItemsPlaylist(playlistId: String, tracks: MutableList<Track>) =
        AddItemsPlaylist(context, playlistId, tracks)
}