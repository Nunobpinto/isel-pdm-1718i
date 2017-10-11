package isel.pdm.moviedatabaseapp.service

import isel.pdm.moviedatabaseapp.model.Movie
import java.io.BufferedReader
import java.io.File

class MovieTMDBService : MovieProvider{

    private var API_KEY = readAPIKEY()
    private val MOVIESBYNAMEURL = "https://api.themoviedb.org/3/search/movie?api_key=$(api_key)&language=en-US&page=1&include_adult=false&query=$(name)"
    private val MOVIEDETAILSURL = "https://api.themoviedb.org/3/movie/$(id)?api_key= $(api_key)d&language=en-US"
    private val NOWPLAYINGRURL = "https://api.themoviedb.org/3/movie/now_playing?api_key=$(api_key)&language=en-US&page=1"
    private val UPCOMINGURL = "https://api.themoviedb.org/3/movie/upcoming?api_key=$(api_key)&language=en-US&page=1"
    private val MOSTPOPULARURL = "https://api.themoviedb.org/3/movie/popular?api_key=$(api_key)&language=en-US&page=1"



    override fun getMoviesByName(name: String): List<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNowPlayingMovies(): List<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMostPopularMovies(): List<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovieDetails(id: Int): Movie {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun readAPIKEY():String{
        val bufferedReader: BufferedReader = File("api_key.txt").bufferedReader()
        return bufferedReader.use { it.readText() }
    }

}
