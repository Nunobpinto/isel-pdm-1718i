package pdm.isel.moviedatabaseapp.service

import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.model.Movie

interface MovieProvider {

    fun getMoviesByName(name:String):List<Movie>
    fun getNowPlayingMovies():List<Movie>
    fun getMovieDetails(id:Int):Movie
    fun getMostPopularMovies(application: MovieApplication): List<Movie>
}
