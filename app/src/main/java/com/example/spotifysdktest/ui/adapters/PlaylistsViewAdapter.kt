package com.example.spotifysdktest.ui.adapters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifysdktest.R
import com.example.spotifysdktest.data.models.Playlist
import com.example.spotifysdktest.ui.activities.TrackActivity
import com.example.spotifysdktest.ui.fragments.AddPlaylistDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.playlist_item_layout.view.*
import org.jetbrains.anko.startActivity

class PlaylistsViewAdapter(
    private val playlists: List<Playlist>,
    private val onAddUpdate: () -> Unit = {}
) :
    RecyclerView.Adapter<PlaylistsViewAdapter.SearchViewHolder>() {

    class SearchViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindPlaylist(playlist: Playlist, onAddUpdate: () -> Unit) {
            with(playlist) {
                if (!images.isNullOrEmpty()) {
                    Picasso.get().load(images.last().url).fit().into(itemView.playlist_cover_image)
                } else {
                    itemView.playlist_cover_image.setImageResource(R.drawable.ic_playlist_cover)
                }
                itemView.playlist_author.text = owner.display_name
                itemView.playlist_name.text = name
                itemView.setOnClickListener {
                    itemView.context.startActivity<TrackActivity>(
                        TrackActivity.PLAYLIST_ID to playlist.id,
                        TrackActivity.PLAYLIST_NAME to playlist.name
                    )
                }
                itemView.setOnLongClickListener {
                    val fragment = AddPlaylistDialogFragment()
                    fragment.setOnDismissListener(onAddUpdate)
                    val args = Bundle()
                    args.putString("playlistName", playlist.name)
                    args.putString("playlistId", playlist.id)
                    fragment.arguments = args
                    fragment.show(
                        (containerView.context as AppCompatActivity).supportFragmentManager,
                        "AddPlaylistDialogFragment"
                    )

                    true
                }
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item_layout, parent, false)
        return SearchViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        holder.bindPlaylist(playlists[position], onAddUpdate)
    }
}