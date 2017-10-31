package pdm.isel.moviedatabaseapp.service

import android.content.Context
import com.android.volley.VolleyError
import pdm.isel.moviedatabaseapp.comms.HttpRequest
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.model.MovieDto
import pdm.isel.moviedatabaseapp.model.MovieListDto
import android.net.ConnectivityManager

class MovieTMDBService (apikey:String, lang:String): MovieProvider {
    private var API_KEY :String = apikey
    private val MOVIES_BY_NAME_URL = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=" + lang + "&page=1&query=%s"
    private val MOVIE_DETAILS_URL = "https://api.themoviedb.org/3/movie/%d?api_key=$API_KEY&language=" + lang
    private val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=$API_KEY&language=" + lang + "&page=1"
    private val UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=$API_KEY&language=" + lang + "&page=1"
    private val MOST_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=$API_KEY&language=" + lang + "&page=1"
    private val SIMILAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/%d/similar?api_key=$API_KEY&language=" + lang

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
        val uri = java.lang.String.format(MOVIE_DETAILS_URL, id)
        val req = HttpRequest(
                uri,
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

    override fun getSimilarMovies(id: Int, ctx: Context, successCb: (MovieListDto) -> Unit, errorCb:(VolleyError)-> Unit) {
        if(!isConnected(ctx))
            return errorCb(VolleyError())
        val uri = java.lang.String.format(SIMILAR_MOVIES_URL, id)
        val req = HttpRequest(
                uri,
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

}
