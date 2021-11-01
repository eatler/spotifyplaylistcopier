package com.example.spotifysdktest.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.example.spotifysdktest.extensions.parse
import com.example.spotifysdktest.utils.MySingleton
import org.json.JSONObject

class CurrentUserId(override var context: Context) : ApiGetCall<String> {

    private var endpoint = "https://api.spotify.com/v1/me"

    override fun get(errorCallback: () -> Unit, callback: (result: String) -> Unit) {
        val responseListener = Response.Listener<JSONObject> {
            val userId = it.getString("id")
            callback(userId)
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