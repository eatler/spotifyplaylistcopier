package com.example.spotifysdktest.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.example.spotifysdktest.extensions.parse
import com.example.spotifysdktest.utils.MySingleton
import org.json.JSONObject


class CreatePlaylist(override var context: Context, private val name: String) :
    ApiPostCall<String> {

    var endpoint =
        "https://api.spotify.com/v1/users/${MySingleton.getInstance(context).userId}/playlists"

    override fun post(errorCallback: () -> Unit, callback: (result: String) -> Unit) {

        val postData = JSONObject()
        postData.put("name", name)
        val responseListener = Response.Listener<JSONObject> {
            val playlistId = it.getString("id")
            callback(playlistId)
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