package com.example.spotifysdktest.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifysdktest.R
import com.example.spotifysdktest.data.models.Track
import com.squareup.picasso.Picasso

class TrackViewAdapter(private val tracks: List<Track>) :
    RecyclerView.Adapter<TrackViewAdapter.SearchViewHolder>() {

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.track_cover_image)
        val trackName: TextView = view.findViewById(R.id.track_name)
        val trackAuthor: TextView = view.findViewById(R.id.track_author)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item_layout, parent, false)
        return SearchViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        val names = StringBuilder()
        tracks[position].album.artists.dropLast(1).forEach {
            names.append(it.name).append(", ")
        }
        holder.trackAuthor.text = names.append(tracks[position].album.artists.last().name)
        holder.trackName.text = tracks[position].name
        Picasso.get().load(tracks[position].album.images.last().url).fit().into(holder.imageView)
    }
}