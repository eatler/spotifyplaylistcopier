package com.example.spotifysdktest.data.models

data class Playlist(val id: String, val name: String, val owner: Owner, val images: List<Image>)

data class Owner(val id: String, val display_name: String)

data class Image(val url: String)

data class Track(val id: String, val uri: String, val name: String, val album: Album)

data class Album(val id: String, val artists: List<Artist>, val images: List<Image>)

data class Artist(val id: String, val name: String)