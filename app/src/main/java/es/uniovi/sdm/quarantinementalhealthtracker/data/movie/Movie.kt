package es.uniovi.sdm.quarantinementalhealthtracker.data.movie

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: String,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val overview: String,
    @SerializedName("vote_average")
    val voteAverage:String
)
