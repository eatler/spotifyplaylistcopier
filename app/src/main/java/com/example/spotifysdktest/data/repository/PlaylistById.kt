package com.example.spotifysdktest.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.example.spotifysdktest.data.models.Playlist
import com.example.spotifysdktest.extensions.parse
import com.example.spotifysdktest.utils.MySingleton
import com.google.gson.Gson
import org.json.JSONObject

class PlaylistById(
    override var context: Context,
    playlistId: String
) : ApiGetCall<Playlist> {

    var endpoint = "https://api.spotify.com/v1/playlists/${playlistId}"

    override fun get(errorCallback: () -> Unit, callback: (result: Playlist) -> Unit) {
        val responseListener = Response.Listener<JSONObject> {
            val gson = Gson()
            val playlist: Playlist = gson.fromJson(it.toString(), Playlist::class.java)
            callback(playlist)
        }

        val errorListener = Response.ErrorListener {
            if (it.networkResponse == null) {
                Log.d("ERROR", "No response")
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("ERROR", it.parse())
            }
        }

        val headers = MySingleton.getInstance(context).headers


        call(endpoint, responseListener, errorListener, headers)
    }
}