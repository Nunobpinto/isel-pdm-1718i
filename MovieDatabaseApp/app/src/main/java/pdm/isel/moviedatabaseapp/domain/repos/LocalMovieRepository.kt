package pdm.isel.moviedatabaseapp.domain.repos

import android.content.AsyncQueryHandler
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import pdm.isel.moviedatabaseapp.domain.content.MovieContentProvider
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.domain.repos.base.ILocalRepository
import pdm.isel.moviedatabaseapp.exceptions.RepoException
import pdm.isel.moviedatabaseapp.mapper.toContentValues
import pdm.isel.moviedatabaseapp.mapper.toMovieDto
import pdm.isel.moviedatabaseapp.mapper.toMovieListDto

class LocalMovieRepository(private val ctx: Context) : ILocalRepository {

    override fun getMovieDetails(id: Int, table: String, successCb: (MovieDto) -> Unit, errorCb: (RepoException) -> Unit) {
        val selectionArgs = arrayOf(id.toString())
        val selection = "ID=?"
        val tableUri = when(table) {
            "NOW_PLAYING" -> MovieContentProvider.NOW_PLAYING_URI
            "UPCOMING" -> MovieContentProvider.UPCOMING_URI
            else -> return errorCb(RepoException("Table not found"))
        }
//        MyAsyncQueryHandler(
//                ctx.contentResolver,
//                errorCb,
//                asyncQueryListener = { pair -> successCb(pair.first!!) }
//        ).startQuery(
//                1,
//                "GET_MOVIE",
//                tableUri,
//                null,
//                selection,
//                selectionArgs,
//                null
//        )
        val cursor: Cursor? = ctx.contentResolver.query(
                tableUri,
                null,
                selection,
                selectionArgs,
                null
        )
        if( cursor === null )
            return errorCb(RepoException())
        val res = cursor.toMovieDto()
        cursor.close()
        successCb(res)
    }

    override fun getNowPlayingMovies(page: Int, successCb: (MovieListDto) -> Unit, errorCb: (RepoException) -> Unit) {
        val offset = (page-1) * 20
        val limit = offset + 20
//        MyAsyncQueryHandler(
//                ctx.contentResolver,
//                errorCb,
//                asyncQueryListener = { pair -> successCb(pair.second!!) }
//        ).startQuery(
//                1,
//                "GET_MOVIES",
//                MovieContentProvider.NOW_PLAYING_URI,
//                null,
//                null,
//                null,
//                " limit $limit offset $offset"
//        )
        val cursor: Cursor? = ctx.contentResolver.query(
                MovieContentProvider.NOW_PLAYING_URI,
                null,
                null,
                null,
                " ID limit $limit offset $offset"
        )
        if( cursor === null )
            return errorCb(RepoException())
        val res = cursor.toMovieListDto(page)
        cursor.close()
        successCb(res)
    }

    override fun getUpComingMovies(page: Int, successCb: (MovieListDto) -> Unit, errorCb: (RepoException) -> Unit) {
        val offset = (page-1) * 20
        val limit = offset + 20
//        MyAsyncQueryHandler(
//                ctx.contentResolver,
//                errorCb,
//                asyncQueryListener = { pair -> successCb(pair.second!!) }
//        ).startQuery(
//                1,
//                "GET_MOVIES",
//                MovieContentProvider.UPCOMING_URI,
//                null,
//                null,
//                null,
//                " limit $limit offset $offset"
//        )
        val cursor: Cursor? = ctx.contentResolver.query(
                MovieContentProvider.UPCOMING_URI,
                null,
                null,
                null,
                " ID limit $limit offset $offset"
        )
        if( cursor === null )
            return errorCb(RepoException())
        val res = cursor.toMovieListDto(page)
        cursor.close()
        successCb(res)
    }

    override fun insertMovie(movie: MovieDto, table: String, errorCb: (RepoException) -> Unit) {
        val tableUri: Uri = when(table) {
            "NOW_PLAYING" -> MovieContentProvider.NOW_PLAYING_URI
            "UPCOMING" -> MovieContentProvider.UPCOMING_URI
            else -> throw RepoException("Table not found!")
        }
        MyAsyncQueryHandler(
                ctx.contentResolver,
                errorCb
        ).startInsert(1, null, tableUri, movie.toContentValues())
    }

    override fun deleteTable(table: String, errorCb: (RepoException) -> Unit) {
        val tableUri: Uri =  when(table) {
            "NOW_PLAYING" -> MovieContentProvider.NOW_PLAYING_URI
            "UPCOMING" -> MovieContentProvider.UPCOMING_URI
            else -> return errorCb(RepoException("Table not found!"))
        }
        MyAsyncQueryHandler(
                ctx.contentResolver,
                errorCb
        ).startDelete(
                1,
                null,
                tableUri,
                null,
                null
        )
    }

    override fun followMovie(movieId: Int) {
        updateFollow(movieId, true)
    }

    override fun unfollowMovie(movieId: Int) {
        updateFollow(movieId, false)
    }

    private fun updateFollow(movieId: Int, following: Boolean) {
        val contentValue = ContentValues()
        contentValue.put("followed", following)
        MyAsyncQueryHandler(
                ctx.contentResolver,
                { error -> Unit } //TODO: handle error
        ).startUpdate(
                1,
                null,
                MovieContentProvider.NOW_PLAYING_URI,
                contentValue,
                "ID=?",
                arrayOf(movieId.toString())
        )
    }
}

class MyAsyncQueryHandler(
        contentResolver: ContentResolver,
        private val errorCb: (RepoException) -> Unit,
        private val asyncQueryListener: ((Pair<MovieDto?, MovieListDto?>) -> Unit)? = null,
        private val asyncInsertListener: ((Uri?) -> Unit)? = null,
        private val asyncUpdateListener: ((Int) -> Unit)? = null,
        private val asyncDeleteListener: ((Int) -> Unit)? = null
) : AsyncQueryHandler(contentResolver) {

    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
        if(cursor === null) return errorCb(RepoException("Exception querying database"))
        if(asyncQueryListener === null) return

        val action = cookie as? String
        val res = when(action) {
            "GET_MOVIE" -> cursor.toMovieDto()
            "GET_MOVIES" -> cursor.toMovieListDto(1)
            else -> return errorCb(RepoException("Action not inserted"))
        }
        cursor.close()
        asyncQueryListener.invoke(Pair(res as MovieDto?, null))
    }

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?) {
        if(uri === null) return errorCb(RepoException("Exception inserting in database"))
        if(asyncInsertListener === null) return

        asyncInsertListener.invoke(uri)
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
        if(result < 0) return errorCb(RepoException("Error updating database"))
        if(asyncUpdateListener === null) return

        asyncUpdateListener.invoke(result)
    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {
        if(result < 0) return super.onDeleteComplete(token, cookie, result)
        if(asyncDeleteListener === null) return

        asyncDeleteListener.invoke(result)
    }
}

