package com.example.spotifysdktest.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.example.spotifysdktest.data.models.Playlist
import com.example.spotifysdktest.extensions.parse
import com.example.spotifysdktest.utils.MySingleton
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class UsersPlaylists(override var context: Context) : ApiGetCall<MutableList<Playlist>> {

    var playlists: MutableList<Playlist> = ArrayList()
    private var endpoint = "https://api.spotify.com/v1/me/playlists?limit=50"

    override fun get(errorCallback: () -> Unit, callback: (result: MutableList<Playlist>) -> Unit) {
        val responseListener = Response.Listener<JSONObject> {
            val gson = Gson()
            val jsonArray: JSONArray? = it.optJSONArray("items")
            for (n in 0 until jsonArray!!.length()) {
                try {
                    val `object` = jsonArray.getJSONObject(n)
                    playlists.add(gson.fromJson(`object`.toString(), Playlist::class.java))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            val next = it.getString("next")
            if (next.equals("null")) {
                callback(playlists)
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
            errorCallback()
        }

        val headers = MySingleton.getInstance(context).headers

        call(endpoint, responseListener, errorListener, headers)
    }
}