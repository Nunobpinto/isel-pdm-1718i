package pdm.isel.moviedatabaseapp.domain.repos

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import pdm.isel.moviedatabaseapp.domain.content.MovieContentProvider
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.domain.repos.base.ILocalRepository
import pdm.isel.moviedatabaseapp.exceptions.RepoException
import pdm.isel.moviedatabaseapp.mapper.toContentValues
import pdm.isel.moviedatabaseapp.mapper.toMovieListDto

class LocalMovieRepository(private val ctx: Context) : ILocalRepository {

    override fun getNowPlayingMovies(page: Int): MovieListDto {
        val offset = page * 20
        val limit = offset + 20
        val cursor = ctx.contentResolver.query(
                MovieContentProvider.EXHIBITION_URI,
                null,
                null,
                null,
                "limit $limit offset $offset"
        )
        if (cursor === null)
            throw RepoException("Exception getting  movies")
        val res = cursor.toMovieListDto(page)
        cursor.close()
        return res
    }

    override fun getUpComingMovies(page: Int): MovieListDto {
        val offset = page * 20
        val limit = offset + 20
        val cursor = ctx.contentResolver.query(
                MovieContentProvider.UPCOMING_URI,
                null,
                null,
                null,
                "limit $limit offset $offset"
        )

        if (cursor === null)
            throw RepoException("Exception getting upcoming movies")
        val res = cursor.toMovieListDto(page)
        cursor.close()
        return res
    }

    override fun insertMovies(movieList: MovieListDto, table: String): Int {
        var table_uri: Uri? = null
        when (table) {
            "EXHIBITION" -> table_uri = MovieContentProvider.EXHIBITION_URI
            "UPCOMING" -> table_uri = MovieContentProvider.UPCOMING_URI
        }
        return ctx.contentResolver.bulkInsert(table_uri, movieList.toContentValues())
    }

    override fun deleteTable(table: String): Int {
        var table_uri: Uri? = null
        when (table) {
            "EXHIBITION" -> table_uri = MovieContentProvider.EXHIBITION_URI
            "UPCOMING" -> table_uri = MovieContentProvider.UPCOMING_URI
        }
        return ctx.contentResolver.delete(table_uri, null, null)
    }

    override fun deleteMovies(movieList: MovieListDto, table: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun followMovie(movieId: Int): Int {
        return updateFollow(movieId, true)
    }

    override fun unfollowMovie(movieId: Int): Int {
        return updateFollow(movieId, false)
    }

    private fun updateFollow(movieId: Int, following: Boolean): Int {
        val contentValue = ContentValues()
        contentValue.put("followed", following)
        return ctx.contentResolver.update(
                MovieContentProvider.EXHIBITION_URI,
                contentValue,
                "ID=?",
                arrayOf(movieId.toString())
        )
    }
}
