package com.example.spotifysdktest.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.spotifysdktest.ui.fragments.PlaylistsFragment
import com.example.spotifysdktest.ui.fragments.RecentlyPlayedFragment

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> PlaylistsFragment()
            else -> RecentlyPlayedFragment()
        }
    }
}