package pdm.isel.moviedatabaseapp.service

import android.content.Context
import com.android.volley.VolleyError
import pdm.isel.moviedatabaseapp.HttpRequest
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto
import pdm.isel.moviedatabaseapp.model.dataDto.MovieListDto
import java.io.File

class MovieTMDBService : MovieProvider {
    private var API_KEY = readAPIKEY()
    private val MOVIES_BY_NAME_URL = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=en-US&page=1&include_adult=false&query=%s"
    private val MOVIE_DETAILS_URL = "https://api.themoviedb.org/3/movie/%d?api_key= $API_KEY&language=en-US"
    private val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=$API_KEY&language=en-US&page=1"
    private val UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=$API_KEY&language=en-US&page=1"
    private val MOST_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=$API_KEY&language=en-US&page=1"

    override fun getUpComingMovies(ctx: Context, cb: (MovieListDto) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMoviesByName(name: String, ctx: Context, cb: (MovieListDto) -> Unit) {
        val req = HttpRequest(
                java.lang.String.format(MOVIES_BY_NAME_URL, name),
                MovieListDto::class.java,
                cb,
                {
                    VolleyError()
                }
        )
        (ctx as MovieApplication).requestQueue.add(req)
    }

    override fun getNowPlayingMovies(ctx: Context, cb: (MovieListDto) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovieDetails(id: Int, ctx: Context, cb: (MovieDto) -> Unit) {
        val req = HttpRequest(
                java.lang.String.format(MOVIE_DETAILS_URL, id),
                MovieDto::class.java,
                cb,
                {
                    VolleyError()
                }
        )
        (ctx as MovieApplication).requestQueue.add(req)
    }

    override fun getMostPopularMovies(ctx: Context, cb: (MovieListDto) -> Unit) {
        val req = HttpRequest(
                MOST_POPULAR_URL,
                MovieListDto::class.java,
                cb,
                {
                    VolleyError()
                }
        )
        (ctx as MovieApplication).requestQueue.add(req)
    }

    private fun readAPIKEY():String{
        //val bufferedReader = File(MovieTMDBService::class.java.getResource("/res/api_key.txt")).bufferedReader()
        //return bufferedReader.use { it.readText() }
        return "af53a8fb127279b18d0cdbd065d80e2d"
    }

}
