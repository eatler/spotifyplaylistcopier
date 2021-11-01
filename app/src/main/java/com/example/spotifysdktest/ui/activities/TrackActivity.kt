package com.example.spotifysdktest.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifysdktest.R
import com.example.spotifysdktest.data.repository.ApiCalls
import com.example.spotifysdktest.ui.adapters.TrackViewAdapter
import com.example.spotifysdktest.ui.fragments.AddPlaylistDialogFragment
import com.example.spotifysdktest.viewmodels.TrackActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.find

class TrackActivity : AppCompatActivity() {

    private val toolbar: Toolbar by lazy { find(R.id.toolbar) }

    private val tracksModel by viewModels<TrackActivityViewModel> {
        TrackActivityViewModel.TrackActivityViewModelFactory(
            ApiCalls(this),
            intent.getStringExtra(PLAYLIST_ID)
        )
    }

    private val trackRecyclerView: RecyclerView by lazy { find(R.id.tracks_recycler_view) }
    private val fab: FloatingActionButton by lazy { find(R.id.fab) }


    companion object {
        const val PLAYLIST_ID = "TrackActivity:playlistId"
        const val PLAYLIST_NAME = "TrackActivity:playlistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        toolbar.title = intent.getStringExtra(PLAYLIST_NAME)
        toolbar.navigationIcon = DrawerArrowDrawable(toolbar.context).apply { progress = 1f }
        toolbar.setNavigationOnClickListener { onBackPressed() }

        trackRecyclerView.layoutManager = LinearLayoutManager(this)
        trackRecyclerView.adapter = TrackViewAdapter(ArrayList())
        tracksModel.getTracks().observe(this) {
            trackRecyclerView.adapter = TrackViewAdapter(it)
        }



        fab.setOnClickListener {
            val fragment = AddPlaylistDialogFragment()
            val args = Bundle()
            args.putString("playlistName", intent.getStringExtra(PLAYLIST_NAME))
            args.putString("playlistId", intent.getStringExtra(PLAYLIST_ID))
            fragment.arguments = args
            fragment.show(
                supportFragmentManager,
                "AddPlaylistDialogFragment"
            )
        }
    }
}