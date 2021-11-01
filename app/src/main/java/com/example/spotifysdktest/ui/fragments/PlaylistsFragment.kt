package com.example.spotifysdktest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.spotifysdktest.R
import com.example.spotifysdktest.data.repository.ApiCalls
import com.example.spotifysdktest.ui.adapters.PlaylistsViewAdapter
import com.example.spotifysdktest.viewmodels.PlaylistsFragmentViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaylistsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaylistsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val playlistsViewModel by viewModels<PlaylistsFragmentViewModel> {
        PlaylistsFragmentViewModel.PlaylistsFragmentViewModelFactory(ApiCalls(requireContext()))
    }

    private lateinit var playlistsRecyclerView: RecyclerView
    private lateinit var playlistsSwipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.playlists_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsRecyclerView = view.findViewById(R.id.playlist_recycler_view)
        playlistsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        playlistsRecyclerView.adapter = PlaylistsViewAdapter(ArrayList())
        playlistsViewModel.getUsersPlaylists().observe(viewLifecycleOwner) {
            val scrollState = (playlistsRecyclerView.layoutManager as LinearLayoutManager).onSaveInstanceState()
            playlistsRecyclerView.adapter = PlaylistsViewAdapter(it) {
                playlistsViewModel.loadUsersPlaylists()
            }
            (playlistsRecyclerView.layoutManager as LinearLayoutManager).onRestoreInstanceState(scrollState)
        }

        playlistsViewModel.getIsRefreshing().observe(viewLifecycleOwner) {
            playlistsSwipeRefresh.isRefreshing = it
        }

        playlistsSwipeRefresh = view.findViewById(R.id.playlists_swipe_refresh)
        playlistsSwipeRefresh.setOnRefreshListener {
            playlistsViewModel.loadUsersPlaylists()
        }

    }


    override fun onResume() {
        super.onResume()
        playlistsViewModel.loadUsersPlaylists()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlaylistsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlaylistsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}


