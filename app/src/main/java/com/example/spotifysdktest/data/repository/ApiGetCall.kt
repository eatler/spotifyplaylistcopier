package com.example.spotifysdktest.data.repository

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spotifysdktest.utils.MySingleton
import org.json.JSONObject

interface ApiGetCall<out T> {
    val context: Context

    fun get(errorCallback: () -> Unit = {}, callback: (result: T) -> Unit)

    fun call(
        endpoint: String,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener,
        headers: HashMap<String, String>
    ) {
        val jsonObjectRequest = object :
            JsonObjectRequest(Method.GET, endpoint, null, responseListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }
}