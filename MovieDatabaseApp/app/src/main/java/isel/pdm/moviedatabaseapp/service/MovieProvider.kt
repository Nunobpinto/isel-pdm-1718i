package isel.pdm.moviedatabaseapp.service

import isel.pdm.moviedatabaseapp.model.Movie

interface MovieProvider {

    fun getMoviesByName(name:String):List<Movie>
    fun getNowPlayingMovies():List<Movie>
    fun getMostPopularMovies():List<Movie>
    fun getMovieDetails(id:Int):Movie

}
