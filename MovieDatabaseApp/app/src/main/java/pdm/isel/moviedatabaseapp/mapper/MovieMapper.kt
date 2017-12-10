package pdm.isel.moviedatabaseapp.mapper

import android.content.ContentValues
import android.database.Cursor
import pdm.isel.moviedatabaseapp.domain.content.MovieContentProvider
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto

class MovieMapper {

    fun MovieDto.toContentValues(): ContentValues {
        val result = ContentValues()
        with (MovieContentProvider) {
            result.put(ID, id)
            result.put(TITLE, title)
            result.put(RELEASEDATE, releaseDate)
            result.put(POSTER, poster)
            result.put(VOTEAVERAGE, voteAverage)
            result.put(RUNTIME, runtime)
            result.put(OVERVIEW, overview)
            //result.put(SIMILAR, similar!!.map { it. })
            result.put(POPULARITY, popularity)
            result.put(GENRES, genres!!.map { it.name }.joinToString(","))
        }
        return result
    }

    fun MovieListDto.toContentValues() : Array<ContentValues> =
            results.map { it.toContentValues() }.toTypedArray()

    private fun toMovieDto(cursor: Cursor): MovieDto {
        with (MovieContentProvider.Companion) {
            return MovieDto(
                    id = cursor.getInt(0),
                    title = cursor.getString(1),
                    runtime = cursor.getInt(5),
                    releaseDate = cursor.getString(2),
                    poster = cursor.getString(3),
                    voteAverage = cursor.getFloat(4),
                    overview = cursor.getString(6),
                    popularity = cursor.getFloat(7),
                    genres = cursor.getString(8)
            )
        }
    }

    fun Cursor.toMovieListDto(): List<MovieDto> {

        val iter = object : AbstractIterator<MovieDto>() {
            override fun computeNext() {
                when (isAfterLast) {
                    true -> done()
                    false -> setNext(toMovieDto(this@toMovieListDto))
                }
            }
        }
        return mutableListOf<MovieDto>().let {
            it.addAll(Iterable { iter }); it
        }
    }
}