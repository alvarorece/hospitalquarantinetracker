package es.uniovi.sdm.quarantinementalhealthtracker.repository

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object MovieRepository {
    private var api : MovieAPI
    init{
        val url = "https://api.themoviedb.org/3/"
        val httpClient = OkHttpClient.Builder()
        val retrofit = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build()).build()
        api= retrofit.create<MovieAPI>()
    }

    suspend fun getRandomMovieTitle (): String {
        val movies = api.getTopMovies().await()
        return movies.results.random().title
    }
}