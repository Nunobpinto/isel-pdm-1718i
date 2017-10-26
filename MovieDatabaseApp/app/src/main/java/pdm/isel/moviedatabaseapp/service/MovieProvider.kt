package pdm.isel.moviedatabaseapp.service

import android.content.Context
import com.android.volley.VolleyError
import pdm.isel.moviedatabaseapp.model.MovieDto
import pdm.isel.moviedatabaseapp.model.MovieListDto

interface MovieProvider {

    fun getMoviesByName(name:String, ctx: Context, successCb: (MovieListDto)->Unit, errorCb: (VolleyError)->Unit)
    fun getNowPlayingMovies(ctx:Context, successCb: (MovieListDto)->Unit, errorCb: (VolleyError)->Unit)
    fun getMovieDetails(id:Int, ctx:Context, successCb: (MovieDto)->Unit, errorCb: (VolleyError)->Unit )
    fun getMostPopularMovies(ctx: Context, successCb: (MovieListDto)->Unit, errorCb: (VolleyError)->Unit)
    fun getUpComingMovies(ctx:Context, successCb: (MovieListDto)->Unit, errorCb: (VolleyError)->Unit)

}
