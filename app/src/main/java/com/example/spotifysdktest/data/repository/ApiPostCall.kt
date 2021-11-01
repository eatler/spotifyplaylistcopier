package com.example.spotifysdktest.data.repository

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spotifysdktest.utils.MySingleton
import org.json.JSONObject

interface ApiPostCall<out T> {
    val context: Context

    fun post(errorCallback: () -> Unit = {}, callback: (result: T) -> Unit)

    fun call(
        endpoint: String,
        postData: JSONObject,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener,
        headers: HashMap<String, String>
    ) {
        val jsonObjectRequest = object :
            JsonObjectRequest(Method.POST, endpoint, postData, responseListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }
}