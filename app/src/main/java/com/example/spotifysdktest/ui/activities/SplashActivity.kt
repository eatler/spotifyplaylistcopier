package com.example.spotifysdktest.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifysdktest.R
import com.example.spotifysdktest.data.repository.ApiCalls
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class SplashActivity : AppCompatActivity() {
    companion object {
        private const val CLIENT_ID = "836c4419aeb8409ba3602a17c8e6d698"
        private const val REDIRECT_URI = "com.example.spotifysdktest://callback"
        private const val SCOPES =
            "user-read-recently-played,user-library-modify,user-read-email,user-read-private, playlist-read-private, playlist-read-collaborative, playlist-modify-public, playlist-modify-private, "
        private const val REQUEST_CODE = 1337
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)
        authenticateSpotify()
    }

    private fun authenticateSpotify() {
        val builder = AuthorizationRequest.Builder(
            CLIENT_ID, AuthorizationResponse.Type.TOKEN,
            REDIRECT_URI
        ).setScopes(arrayOf(SCOPES))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(
            this,
            REQUEST_CODE, request
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val editor = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE).edit()
                    editor.putString("token", response.accessToken)
                    editor.apply()
                }
                AuthorizationResponse.Type.ERROR -> {
                }
                else -> {
                }
            }
        }

        AuthorizationClient.clearCookies(applicationContext)

        if (getSharedPreferences("SPOTIFY", 0).getString("userId", "").toString() == "") {
            ApiCalls(this).getCurrentUserId().get {
                val editor = getSharedPreferences("SPOTIFY", Context.MODE_PRIVATE).edit()
                editor.putString("userId", it)
                editor.apply()
                startActivity(Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
        } else {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

    }

}