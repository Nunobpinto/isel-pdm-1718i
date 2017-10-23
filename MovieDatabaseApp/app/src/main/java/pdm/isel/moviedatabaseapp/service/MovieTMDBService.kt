package pdm.isel.moviedatabaseapp.service

import android.content.Context
import android.util.Log
import com.android.volley.VolleyError
import pdm.isel.moviedatabaseapp.HttpRequest
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.model.Movie
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto
import pdm.isel.moviedatabaseapp.model.dataDto.SearchDto
import java.io.File

class MovieTMDBService : MovieProvider {


    override fun getUpComingMovies(ctx: Context, cb: (SearchDto) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun getMoviesByName(name: String, ctx: Context, cb: (SearchDto) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNowPlayingMovies(ctx: Context, cb: (SearchDto) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovieDetails(id: Int, ctx: Context, cb: (MovieDto) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var API_KEY = readAPIKEY()
    private val MOVIESBYNAMEURL = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=en-US&page=1&include_adult=false&query=§(name)"
    private val MOVIEDETAILSURL = "https://api.themoviedb.org/3/movie/$(id)?api_key= $API_KEY&language=en-US"
    private val NOWPLAYINGRURL = "https://api.themoviedb.org/3/movie/now_playing?api_key=$API_KEY&language=en-US&page=1"
    private val UPCOMINGURL = "https://api.themoviedb.org/3/movie/upcoming?api_key=$API_KEY&language=en-US&page=1"
    private val MOSTPOPULARURL = "https://api.themoviedb.org/3/movie/popular?api_key=$API_KEY&language=en-US&page=1"


    override fun getMostPopularMovies(ctx: Context, cb: (SearchDto) -> Unit) {
        val req = HttpRequest(
                MOSTPOPULARURL,
                SearchDto::class.java,
                cb,
                {
                    VolleyError()
                }
        )
        (ctx as MovieApplication).let { it.requestQueue.add(req) }
    }





    private fun readAPIKEY():String{
        val bufferedReader = File("api_key.txt").bufferedReader()
        return bufferedReader.use { it.readText() }
    }

}
