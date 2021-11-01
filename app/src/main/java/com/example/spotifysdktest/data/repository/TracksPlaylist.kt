package com.example.spotifysdktest.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.example.spotifysdktest.data.models.Track
import com.example.spotifysdktest.extensions.parse
import com.example.spotifysdktest.utils.MySingleton
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TracksPlaylist(override var context: Context, playlistId: String) :
    ApiGetCall<MutableList<Track>> {

    private var tracks: MutableList<Track> = ArrayList()
    private var offset = 0
    private var endpoint =
        "https://api.spotify.com/v1/playlists/$playlistId/tracks?fields=total%2Cnext%2Citems(track(album(id%2Cartists%2Cimages)%2C%20id%2C%20name%2Curi))&limit=100&offset=$offset"

    override fun get(errorCallback: () -> Unit, callback: (result: MutableList<Track>) -> Unit) {
        val responseListener = Response.Listener<JSONObject> {
            val gson = Gson()
            val jsonArray: JSONArray = it.optJSONArray("items") ?: JSONArray()
            for (n in 0 until jsonArray.length()) {
                try {
                    val `object` = jsonArray.getJSONObject(n).getJSONObject("track")
                    tracks.add(gson.fromJson(`object`.toString(), Track::class.java))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            val next = it.getString("next")
            if (next.equals("null")) {
                callback(tracks)
            } else {
                endpoint = next
                get(callback = callback)
            }
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