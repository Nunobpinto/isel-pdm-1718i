package pdm.isel.moviedatabaseapp.domain.repos.base

import pdm.isel.moviedatabaseapp.domain.model.MovieListDto

interface ILocalRepository {
    fun getNowPlayingMovies() : MovieListDto
    fun getUpComingMovies() : MovieListDto
    fun insertMovies(movieList : MovieListDto) : Int
    fun deleteMovies(movieList : MovieListDto) : Int
    fun followMovie(movieId : Int)
}
