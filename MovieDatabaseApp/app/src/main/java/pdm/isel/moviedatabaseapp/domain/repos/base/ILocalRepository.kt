package pdm.isel.moviedatabaseapp.domain.repos.base

import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.exceptions.RepoException

interface ILocalRepository {

    fun getNowPlayingMovies(page: Int, successCb: (MovieListDto) -> Unit, errorCb: (RepoException) -> Unit)
    fun getUpComingMovies(page: Int, successCb: (MovieListDto) -> Unit, errorCb: (RepoException) -> Unit)
    fun getMovieDetails(id: Int, table: String, successCb: (MovieDto) -> Unit, errorCb: (RepoException) -> Unit)
    fun insertMovie(movie: MovieDto, table: String, errorCb: (RepoException) -> Unit)
    fun deleteTable(table: String, errorCb: (RepoException) -> Unit)
    fun followMovie(movieId: Int)
    fun unfollowMovie(movieId: Int)
}
