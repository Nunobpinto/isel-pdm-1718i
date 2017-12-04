package pdm.isel.moviedatabaseapp.domain.providers

import android.content.Context
import com.android.volley.VolleyError
import pdm.isel.moviedatabaseapp.comms.HttpRequest
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import android.net.ConnectivityManager

class MovieTMDBProvider(apikey: String, lang: String) : MovieProvider {
    private var API_KEY: String = apikey
    private val MOVIES_BY_NAME_URL = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=" + lang + "&query=%s&page="
    private val MOVIE_DETAILS_URL = "https://api.themoviedb.org/3/movie/%d?api_key=$API_KEY&language=" + lang
    private val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=$API_KEY&language=" + lang + "&page="
    private val UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=$API_KEY&language=" + lang + "&page="
    private val MOST_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=$API_KEY&language=" + lang + "&page="
    private val SIMILAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/%d/similar?api_key=$API_KEY&language=" + lang


    override fun getUpComingMovies(page: Int, ctx: Context, successCb: (MovieListDto) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx))
            return errorCb(VolleyError())
        val url = UPCOMING_URL + page
        val req = HttpRequest(
                url,
                MovieListDto::class.java,
                successCb,
                errorCb
        )
        (ctx as MovieApplication).let { it.requestQueue.add(req) }
    }

    override fun getMoviesByName(name: String, page: Int, ctx: Context, successCb: (MovieListDto) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx))
            return errorCb(VolleyError())
        val url = java.lang.String.format(MOVIES_BY_NAME_URL, name) + page
        val req = HttpRequest(
                url,
                MovieListDto::class.java,
                successCb,
                errorCb
        )
        (ctx as MovieApplication).requestQueue.add(req)
    }

    override fun getNowPlayingMovies(page: Int, ctx: Context, successCb: (MovieListDto) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx))
            return errorCb(VolleyError())
        val url = NOW_PLAYING_URL + page
        val req = HttpRequest(
                url,
                MovieListDto::class.java,
                successCb,
                errorCb
        )
        (ctx as MovieApplication).let { it.requestQueue.add(req) }
    }

    override fun getMovieDetails(id: Int, ctx: Context, successCb: (MovieDto) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx))
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

    override fun getMostPopularMovies(page: Int, ctx: Context, successCb: (MovieListDto) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx))
            return errorCb(VolleyError())
        val url =  MOST_POPULAR_URL + page
        val req = HttpRequest(
                url,
                MovieListDto::class.java,
                successCb,
                errorCb
        )
        (ctx as MovieApplication).requestQueue.add(req)
    }

    override fun getSimilarMovies(id: Int, ctx: Context, successCb: (MovieListDto) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx))
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
