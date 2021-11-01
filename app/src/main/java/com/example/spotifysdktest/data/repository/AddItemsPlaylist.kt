package com.example.spotifysdktest.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.example.spotifysdktest.data.models.Track
import com.example.spotifysdktest.extensions.parse
import com.example.spotifysdktest.utils.MySingleton
import org.json.JSONArray
import org.json.JSONObject

class AddItemsPlaylist(
    override var context: Context,
    playlistId: String,
    private val tracks: MutableList<Track>
) : ApiPostCall<String> {

    var endpoint = "https://api.spotify.com/v1/playlists/${playlistId}/tracks"

    override fun post(errorCallback: () -> Unit, callback: (result: String) -> Unit) {

        val postData = JSONObject()
        val trackUriArray = JSONArray()
        for (track in tracks) {
            trackUriArray.put(track.uri)
        }
        postData.put("uris", trackUriArray)
        val responseListener = Response.Listener<JSONObject> {
            callback("")
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

        call(endpoint, postData, responseListener, errorListener, headers)
    }
}