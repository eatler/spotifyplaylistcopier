package com.example.spotifysdktest.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.example.spotifysdktest.data.models.Playlist
import com.example.spotifysdktest.extensions.parse
import com.example.spotifysdktest.utils.MySingleton
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class RecentPlaylists(override var context: Context) : ApiGetCall<MutableList<Playlist>> {

    var playlistsIds: MutableList<String> = ArrayList()
    var playlists: MutableList<Playlist> = ArrayList()
    var index: Int = 0
    private val endpoint = "https://api.spotify.com/v1/me/player/recently-played?limit=50"

    override fun get(errorCallback: () -> Unit, callback: (result: MutableList<Playlist>) -> Unit) {
        val responseListener = Response.Listener<JSONObject> {
            val jsonArray: JSONArray? = it.optJSONArray("items")
            for (n in 0 until jsonArray!!.length()) {
                try {
                    var `object` = jsonArray.getJSONObject(n)
                    `object` = `object`.optJSONObject("context")
                    if (`object`?.getString("type") == "playlist") {
                        val playlistId: String = `object`.getString("uri")
                        val id = playlistId.split(':')[2]
                        if (!playlistsIds.contains(id)) {
                            playlistsIds.add(id)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            getPlaylistRecursionCall(callback)
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

    private fun getPlaylistRecursionCall(callback: (result: MutableList<Playlist>) -> Unit) {
        if (index == playlistsIds.size) {
            callback(playlists)
            return
        }
        ApiCalls(context).getRecentPlaylists(playlistsIds[index]).get { playlist ->
            playlists.add(playlist)
            index++
            getPlaylistRecursionCall(callback)
        }
    }
}