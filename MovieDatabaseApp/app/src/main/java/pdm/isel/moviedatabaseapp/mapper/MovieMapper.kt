package pdm.isel.moviedatabaseapp.mapper

import android.content.ContentValues
import android.database.Cursor
import pdm.isel.moviedatabaseapp.domain.content.MovieContentProvider
import pdm.isel.moviedatabaseapp.domain.model.Genres
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto

fun MovieDto.toContentValues(): ContentValues {
    val result = ContentValues()
    with(MovieContentProvider) {
        result.put(ID, id)
        result.put(TITLE, title)
        result.put(RELEASE_DATE, releaseDate)
        result.put(POSTER, poster)
        result.put(VOTE_AVERAGE, voteAverage)
        result.put(RUNTIME, runtime)
        result.put(OVERVIEW, overview)
        result.put(POPULARITY, popularity)
        result.put(GENRES, genres?.map { it.name }?.joinToString(", "))
    }
    return result
}

fun MovieListDto.toContentValues(): Array<ContentValues> =
        results.map(MovieDto::toContentValues).toTypedArray()

fun Cursor.toMovieListDto(page: Int): MovieListDto {
    val iter = object : AbstractIterator<MovieDto>() {
        override fun computeNext() {
            this@toMovieListDto.moveToNext()
            when (isAfterLast) {
                true -> done()
                false -> setNext(mapToMovieDto(this@toMovieListDto))
            }
        }
    }
    val list = mutableListOf<MovieDto>().let { it.addAll(Iterable { iter }); it }.toTypedArray()
    return MovieListDto(
            page,
            list.size,
            null,
            list,
            null
    )
}

fun Cursor.toMovieDto(): MovieDto {
    val iter = object : AbstractIterator<MovieDto>() {
        override fun computeNext() {
            this@toMovieDto.moveToNext()
            when (isAfterLast) {
                true -> done()
                false -> setNext(mapToMovieDto(this@toMovieDto))
            }
        }
    }
    val list = mutableListOf<MovieDto>().let { it.addAll(Iterable { iter }); it }.toTypedArray()
    return list.first()
}

fun mapToMovieDto(cursor: Cursor): MovieDto {
    return MovieDto(
            id = cursor.getInt(cursor.getColumnIndex(MovieContentProvider.ID)),
            title = cursor.getString(cursor.getColumnIndex(MovieContentProvider.TITLE)),
            runtime = cursor.getInt(cursor.getColumnIndex(MovieContentProvider.RUNTIME)),
            releaseDate = cursor.getString(cursor.getColumnIndex(MovieContentProvider.RELEASE_DATE)),
            poster = cursor.getString(cursor.getColumnIndex(MovieContentProvider.POSTER)),
            voteAverage = cursor.getFloat(cursor.getColumnIndex(MovieContentProvider.VOTE_AVERAGE)),
            overview = cursor.getString(cursor.getColumnIndex(MovieContentProvider.OVERVIEW)),
            popularity = cursor.getFloat(cursor.getColumnIndex(MovieContentProvider.POPULARITY)),
            genres = Genres.create(cursor.getString(cursor.getColumnIndex(MovieContentProvider.GENRES)))
    )
}