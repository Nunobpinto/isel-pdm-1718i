package pdm.isel.moviedatabaseapp.domain.repos

import android.content.Context
import android.database.Cursor
import pdm.isel.moviedatabaseapp.domain.content.MovieContentProvider
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.domain.repos.base.ILocalRepository
import pdm.isel.moviedatabaseapp.mapper.toMovieListDto

class LocalMovieRepository(private val ctx : Context) : ILocalRepository {

    override fun getNowPlayingMovies(): List<MovieDto> {
        val c = ctx.contentResolver.query(
                MovieContentProvider.EXHIBITION_URI,
                null,
                null,
                null,
                null
        )
        val res :List<MovieDto>
        if(c!== null){
            res = c.toMovieListDto()
        }
        else throw Exception("Exception getting exhibition movies")
        c.close()
        return res
    }

    override fun getUpComingMovies(): List<MovieDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertMovies(movieList: MovieListDto): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteMovies(movieList: MovieListDto): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun followMovie(movieId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
