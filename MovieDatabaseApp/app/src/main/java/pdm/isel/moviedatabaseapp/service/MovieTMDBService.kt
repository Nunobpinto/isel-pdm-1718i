package pdm.isel.moviedatabaseapp.service

import android.util.Log
import com.android.volley.VolleyError
import pdm.isel.moviedatabaseapp.HttpRequest
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.model.Movie
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto
import java.io.File

class MovieTMDBService : MovieProvider {

    private var API_KEY = readAPIKEY()
    private val MOVIESBYNAMEURL = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=en-US&page=1&include_adult=false&query=§(name)"
    private val MOVIEDETAILSURL = "https://api.themoviedb.org/3/movie/$(id)?api_key= $API_KEY&language=en-US"
    private val NOWPLAYINGRURL = "https://api.themoviedb.org/3/movie/now_playing?api_key=$API_KEY&language=en-US&page=1"
    private val UPCOMINGURL = "https://api.themoviedb.org/3/movie/upcoming?api_key=$API_KEY&language=en-US&page=1"
    private val MOSTPOPULARURL = "https://api.themoviedb.org/3/movie/popular?api_key=$API_KEY&language=en-US&page=1"


    override fun getMoviesByName(name: String): List<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNowPlayingMovies(): List<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMostPopularMovies(application:MovieApplication): List<Movie> {
        var list : List<Movie> = ArrayList()
        val req : HttpRequest<Array<MovieDto>>
                = HttpRequest(
                        MOSTPOPULARURL,
                        Array<MovieDto>::class.java,
                    {
                        movies-> list = movies.map { movieDto ->
                                    Movie(movieDto.title,movieDto.id,movieDto.releaseDate.toInt(),movieDto.voteAverage.toString(),movieDto.poster,movieDto.overview) }
                    },
                    {
                        VolleyError()
                    })
        application.requestQueue.add(req)
        return list
    }

    override fun getMovieDetails(id: Int): Movie {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun readAPIKEY():String{
        val bufferedReader = File("api_key.txt").bufferedReader()
        return bufferedReader.use { it.readText() }
    }

}
