package pdm.isel.moviedatabaseapp.domain.repos.base;

import android.content.Context;
import com.android.volley.VolleyError
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto

interface ITMDBMovieRepository {
    fun getMoviesByName(name: String, page: Int, ctx: Context, successCb: (MovieListDto, String) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getNowPlayingMovies(page: Int, ctx: Context, successCb: (MovieListDto, String) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getMovieDetails(id: Int, ctx: Context, successCb: (MovieDto, String) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getMostPopularMovies(page: Int, ctx: Context, successCb: (MovieListDto, String) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getUpComingMovies(page: Int, ctx: Context, successCb: (MovieListDto, String) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getSimilarMovies(id: Int, ctx: Context, successCb: (MovieListDto, String) -> Unit, errorCb: (VolleyError) -> Unit)
}
