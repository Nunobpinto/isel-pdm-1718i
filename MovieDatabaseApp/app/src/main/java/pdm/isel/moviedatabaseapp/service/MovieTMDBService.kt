package pdm.isel.moviedatabaseapp.service

import android.content.Context
import com.android.volley.VolleyError
import pdm.isel.moviedatabaseapp.HttpRequest
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto
import pdm.isel.moviedatabaseapp.model.dataDto.MovieListDto
import android.net.ConnectivityManager

class MovieTMDBService : MovieProvider {
    private var API_KEY = readAPIKEY()
    private val MOVIES_BY_NAME_URL = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=en-US&page=1&query=%s"
    private val MOVIE_DETAILS_URL = "https://api.themoviedb.org/3/movie/%d?api_key= $API_KEY&language=en-US"
    private val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=$API_KEY&language=en-US&page=1"
    private val UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=$API_KEY&language=en-US&page=1"
    private val MOST_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=$API_KEY&language=en-US&page=1   "

    override fun getUpComingMovies(ctx: Context, successCb: (MovieListDto) -> Unit, errorCb:(VolleyError)-> Unit) {
        if(!isConnected(ctx))
            return errorCb(VolleyError())
        val req = HttpRequest(
                UPCOMING_URL,
                MovieListDto::class.java,
                successCb,
                errorCb
        )
        (ctx as MovieApplication).let { it.requestQueue.add(req) }
    }

    override fun getMoviesByName(name: String, ctx: Context, successCb: (MovieListDto) -> Unit, errorCb:(VolleyError)-> Unit) {
        if(!isConnected(ctx))
            return errorCb(VolleyError())
        val req = HttpRequest(
                java.lang.String.format(MOVIES_BY_NAME_URL, name),
                MovieListDto::class.java,
                successCb,
                errorCb
        )
        (ctx as MovieApplication).requestQueue.add(req)
    }

    override fun getNowPlayingMovies(ctx: Context, successCb: (MovieListDto) -> Unit, errorCb:(VolleyError)-> Unit) {
        if(!isConnected(ctx))
            return errorCb(VolleyError())
        val req = HttpRequest(
                NOW_PLAYING_URL,
                MovieListDto::class.java,
                successCb,
                errorCb
        )
        (ctx as MovieApplication).let { it.requestQueue.add(req) }
    }

    override fun getMovieDetails(id: Int, ctx: Context, successCb: (MovieDto) -> Unit, errorCb:(VolleyError)-> Unit) {
        if(!isConnected(ctx))
            return errorCb(VolleyError())
        val req = HttpRequest(
                java.lang.String.format(MOVIE_DETAILS_URL, id),
                MovieDto::class.java,
                successCb,
                errorCb
        )
        (ctx as MovieApplication).requestQueue.add(req)
    }

    override fun getMostPopularMovies(ctx: Context, successCb: (MovieListDto) -> Unit, errorCb:(VolleyError)-> Unit) {
        if(!isConnected(ctx))
            return errorCb(VolleyError())
        val req = HttpRequest(
                MOST_POPULAR_URL,
                MovieListDto::class.java,
                successCb,
                errorCb
        )
        (ctx as MovieApplication).requestQueue.add(req)
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun readAPIKEY():String{
        //val bufferedReader = File(MovieTMDBService::class.java.getResource("/res/api_key.txt")).bufferedReader()
        //return bufferedReader.use { it.readText() }
        return "af53a8fb127279b18d0cdbd065d80e2d"
    }

}
