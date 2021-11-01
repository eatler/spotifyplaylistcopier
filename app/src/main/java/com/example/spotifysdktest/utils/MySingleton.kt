package com.example.spotifysdktest.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class MySingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: MySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: MySingleton(context).also {
                        INSTANCE = it
                    }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    val token: String by lazy {
        context.getSharedPreferences("SPOTIFY", 0).getString("token", "").toString()
    }

    val headers: HashMap<String, String> by lazy {
        hashMapOf("Authorization" to "Bearer $token")
    }

    val userId: String by lazy {
        context.getSharedPreferences("SPOTIFY", 0).getString("userId", "").toString()
    }
}