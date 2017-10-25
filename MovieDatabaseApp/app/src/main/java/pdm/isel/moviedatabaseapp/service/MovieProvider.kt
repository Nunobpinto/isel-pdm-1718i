package pdm.isel.moviedatabaseapp.service

import android.content.Context
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto
import pdm.isel.moviedatabaseapp.model.dataDto.MovieListDto

interface MovieProvider {

    fun getMoviesByName(name:String,ctx: Context,cb : (MovieDto)->Unit)
    fun getNowPlayingMovies(ctx:Context,cb : (MovieListDto)->Unit)
    fun getMovieDetails(id:Int,ctx:Context,cb : (MovieDto)->Unit )
    fun getMostPopularMovies(ctx: Context,cb : (MovieListDto)->Unit)
    fun getUpComingMovies(ctx:Context,cb : (MovieListDto)->Unit)
}
