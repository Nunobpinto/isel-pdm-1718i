package pdm.isel.moviedatabaseapp.domain.repos.base

import pdm.isel.moviedatabaseapp.domain.model.MovieListDto

interface ILocalRepository {
    fun getNowPlayingMovies(page: Int) : MovieListDto
    fun getUpComingMovies(page: Int) : MovieListDto
    fun insertMovies(movieList : MovieListDto, table : String) : Int
    fun deleteMovies(movieList : MovieListDto, table : String) : Int
    fun followMovie(movieId : Int): Int
    fun unfollowMovie(movieId: Int): Int
    fun deleteTable(table: String): Int
}
