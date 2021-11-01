package com.example.spotifysdktest.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.spotifysdktest.R
import com.example.spotifysdktest.ui.fragments.MainFragment
import com.example.spotifysdktest.ui.fragments.SearchFragment
import com.example.spotifysdktest.utils.MySingleton

class MainActivity : AppCompatActivity() {

    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //Log.d("TOKEN", MySingleton.getInstance(applicationContext).token)

        if (savedInstanceState == null) supportFragmentManager.commit {
            replace<MainFragment>(R.id.main_fragment_container)
        }
        //Log.d("USERID", getSharedPreferences("SPOTIFY", 0).getString("userId", "").toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout_action -> {
                    startActivity(Intent(this, SplashActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                R.id.search_action -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<SearchFragment>(R.id.main_fragment_container)
                        addToBackStack(null)
                    }
                }
                else -> {
                }
            }
            true
        }

        val searchView = menu?.findItem(R.id.search_action)?.actionView as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.apply {
            queryHint = resources.getString(R.string.search_hint)
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }
}