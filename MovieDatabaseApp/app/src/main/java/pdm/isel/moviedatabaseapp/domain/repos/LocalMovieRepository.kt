package pdm.isel.moviedatabaseapp.domain.repos

import android.content.Context
import android.database.Cursor
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.domain.repos.base.ILocalRepository
import pdm.isel.moviedatabaseapp.mapper.toMovieListDto

class LocalMovieRepository(val ctx : Context) : ILocalRepository {

    override fun getNowPlayingMovies(): MovieListDto {
        val c = ctx.contentResolver.query(null,null,null,null,null)
        if(c!== null){
            c.toMovieListDto()
        }
        throw UnsupportedOperationException("")
    }

    override fun getUpComingMovies(): MovieListDto {
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
