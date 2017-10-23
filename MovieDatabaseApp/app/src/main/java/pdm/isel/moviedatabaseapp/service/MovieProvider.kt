package pdm.isel.moviedatabaseapp.service

import android.content.Context
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.model.Movie
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto
import pdm.isel.moviedatabaseapp.model.dataDto.SearchDto

interface MovieProvider {

    fun getMoviesByName(name:String,ctx: Context,cb : (SearchDto)->Unit)
    fun getNowPlayingMovies(ctx:Context,cb : (SearchDto)->Unit)
    fun getMovieDetails(id:Int,ctx:Context,cb : (MovieDto)->Unit )
    fun getMostPopularMovies(ctx: Context,cb : (SearchDto)->Unit)
    fun getUpComingMovies(ctx:Context,cb : (SearchDto)->Unit)
}
