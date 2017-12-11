package pdm.isel.moviedatabaseapp.domain.repos

import android.content.Context
import android.net.Uri
import pdm.isel.moviedatabaseapp.domain.content.MovieContentProvider
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.domain.repos.base.ILocalRepository
import pdm.isel.moviedatabaseapp.mapper.toContentValues
import pdm.isel.moviedatabaseapp.mapper.toMovieListDto

class LocalMovieRepository(private val ctx : Context) : ILocalRepository {

    override fun getNowPlayingMovies() : List<MovieDto> {
        val cursor = ctx.contentResolver.query(
                MovieContentProvider.EXHIBITION_URI,
                null,
                null,
                null,
                null
        )
        val res : List<MovieDto>
        if(cursor !== null){
            res = cursor.toMovieListDto()
        }
        else throw Exception("Exception getting exhibition movies")
        cursor.close()
        return res
    }

    override fun getUpComingMovies(): List<MovieDto> {
        val cursor = ctx.contentResolver.query(
                MovieContentProvider.UPCOMING_URI,
                null,
                null,
                null,
                null
        )
        val res : List<MovieDto>
        if(cursor !== null){
            res = cursor.toMovieListDto()
        }
        else throw Exception("Exception getting upcoming movies")
        cursor.close()
        return res
    }

    override fun insertMovies(movieList: MovieListDto, table : String): Int {
        var table_uri : Uri? = null
        when(table){
            "EXHIBITION" -> table_uri = MovieContentProvider.EXHIBITION_URI
            "UPOCOMING" -> table_uri = MovieContentProvider.UPCOMING_URI
        }
        return ctx.contentResolver.bulkInsert(table_uri, movieList.toContentValues())
    }

    override fun deleteMovies(movieList: MovieListDto, table: String): Int {
        var table_uri : Uri? = null
        when(table){
            "EXHIBITION" -> table_uri = MovieContentProvider.EXHIBITION_URI
            "UPOCOMING" -> table_uri = MovieContentProvider.UPCOMING_URI
        }
        return ctx.contentResolver.delete(table_uri, null, null)
    }

    override fun followMovie(movieId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
