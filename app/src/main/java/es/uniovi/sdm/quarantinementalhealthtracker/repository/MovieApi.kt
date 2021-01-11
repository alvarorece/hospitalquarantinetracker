package es.uniovi.sdm.quarantinementalhealthtracker.repository

import es.uniovi.sdm.quarantinementalhealthtracker.data.movie.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface MovieAPI {

    @GET("discover/movie?api_key=18e0bb4390ef6a403b26c9f2b4b62312&language=en-US&sort_by=vote_average.desc&include_video=false&page=1")
    fun getTopMovies() : Call<MovieResponse>
}