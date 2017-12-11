package pdm.isel.moviedatabaseapp.mapper

import android.content.ContentValues
import android.database.Cursor
import pdm.isel.moviedatabaseapp.domain.content.MovieContentProvider
import pdm.isel.moviedatabaseapp.domain.model.Genres
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto

    fun MovieDto.toContentValues(): ContentValues {
        val result = ContentValues()
        with (MovieContentProvider) {
            result.put(ID, id)
            result.put(TITLE, title)
            result.put(RELEASE_DATE, releaseDate)
            result.put(POSTER, poster)
            result.put(VOTE_AVERAGE, voteAverage)
            result.put(RUNTIME, runtime)
            result.put(OVERVIEW, overview)
            //result.put(SIMILAR, similar!!.map { it. })
            result.put(POPULARITY, popularity)
            result.put(GENRES, genres!!.map { it.name }.joinToString(","))
        }
        return result
    }

    fun MovieListDto.toContentValues() : Array<ContentValues> =
            results.map(MovieDto::toContentValues).toTypedArray()

    private fun toMovieDto(cursor: Cursor): MovieDto {
        with (MovieContentProvider.Companion) {
            return MovieDto(
                    id = cursor.getInt(0),
                    title = cursor.getString(1),
                    runtime = cursor.getInt(2),
                    releaseDate = cursor.getString(3),
                    poster = cursor.getString(4),
                    voteAverage = cursor.getFloat(5),
                    overview = cursor.getString(6),
                    popularity = cursor.getFloat(7),
                    genres = Genres.create(cursor.getString(8))
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
