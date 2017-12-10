package pdm.isel.moviedatabaseapp.domain.content

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class MovieContentProvider : ContentProvider() {

    companion object {

        const val UPCOMING = "Upcoming"
        const val EXHIBITION = "Exhibition"

        //columns of table
        const val ID = "ID"
        const val TITLE = "title"
        const val RELEASEDATE = "release_date"
        const val POSTER = "poster"
        const val VOTEAVERAGE = "vote_average"
        const val RUNTIME = "runtime"
        const val SIMILAR = "similar"
        const val POPULARITY = "popularity"
        const val OVERVIEW = "overview"
        const val GENRES = "genres"

        //configure movie remoteRepository
        const val AUTHORITY = "pdm.isel.moviedatabaseapp"

        //paths for each table
        const val EXHIBITION_PATH = "exhibition"
        const val UPCOMING_PATH = "upcoming"

        //URIs to access db tables
        val EXHIBITION_URI = Uri.parse("content://$AUTHORITY/$EXHIBITION_PATH")
        val UPCOMING_URI = Uri.parse("content://$AUTHORITY/$UPCOMING_PATH")

        //auxiliary code to return when each uri is matched
        const val UPCOMING_LIST_CODE = 100
        const val UPCOMING_ITEM_CODE = 200
        const val EXHIBITION_LIST_CODE = 300
        const val EXHIBITION_ITEM_CODE = 400

        //auxiliary types for getType method
        val MOVIE_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/movies"
        val MOVIE_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/movie"

    }

    @Volatile private lateinit var dbHelper: MovieDbHelper

    /**
     * @property uriMatcher The instance used to match an URI to its corresponding content type
     */
    @Volatile private lateinit var uriMatcher: UriMatcher

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val tableInfo = getTable(uri)
        val db = dbHelper.writableDatabase
        return db.use {
            val id = db.insert(tableInfo.first, null, values)
            if (id < 0) {
                null
            } else {
                context.contentResolver.notifyChange(uri, null)
                Uri.parse("content://$AUTHORITY/${tableInfo.second}/$id")
            }
        }
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, args: Array<String>?, sort: String?): Cursor {
        val params = getSpecificTablee(uri, selection, args)
        val db = dbHelper.readableDatabase
        return db.query(params.first, projection, params.second, params.third, null, null, sort)
    }

    override fun onCreate(): Boolean {
        dbHelper = MovieDbHelper(this@MovieContentProvider.context)
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        with(uriMatcher) {
            addURI(AUTHORITY, UPCOMING_PATH, UPCOMING_LIST_CODE)
            addURI(AUTHORITY, "$UPCOMING_PATH/#", UPCOMING_ITEM_CODE)
            addURI(AUTHORITY, EXHIBITION_PATH, EXHIBITION_LIST_CODE)
            addURI(AUTHORITY, "$EXHIBITION_PATH/#", EXHIBITION_ITEM_CODE)
        }
        return true
    }

    /**
     * Helper function used to obtain the table information (i.e. table name and path) based on the
     * given [uri]
     * @param [uri] The table URI
     * @return A [Pair] instance bearing the table name (the pair's first) and the table path
     * part (the pair's second).
     * @throws IllegalArgumentException if the received [uri] does not refer to an existing table
     */
    private fun getTable(uri: Uri): Pair<String, String> = when (uriMatcher.match(uri)) {
        UPCOMING_LIST_CODE -> Pair(UPCOMING, UPCOMING_PATH)
        EXHIBITION_LIST_CODE -> Pair(EXHIBITION, EXHIBITION_PATH)
        else -> null
    } ?: throw IllegalArgumentException("Uri $uri not supported")

    /**
     * Helper function used to obtain the table name and selection arguments based on the
     * given [uri]
     * @param [uri] The received URI, which may refer to a table or to an individual entry
     * @return A [Triple] instance bearing the table name (the triple's first), the selection
     * string (the triple's second) and the selection string parameters (the triple's third).
     * @throws IllegalArgumentException if the received [uri] does not refer to a valid data set
     */
    private fun getSpecificTablee(uri: Uri, selection: String?, selectionArgs: Array<String>?)
            : Triple<String, String?, Array<String>?> {
        val itemSelection = "$ID = ${uri.pathSegments.last()}"
        return when (uriMatcher.match(uri)) {
            UPCOMING_ITEM_CODE -> Triple(UPCOMING, itemSelection, null)
            EXHIBITION_ITEM_CODE -> Triple(EXHIBITION, itemSelection, null)
            else -> getTable(uri).let { Triple(it.first, selection, selectionArgs) }
        }
    }

    override fun update(uri: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int =
            throw UnsupportedOperationException("Upgrade not implemented yet!!!!")

    override fun delete(uri: Uri, selection: String?, args: Array<String>?): Int {
        val params = getSpecificTablee(uri, selection, args)
        val db = dbHelper.writableDatabase
        db.use {
            val deletedCount = db.delete(params.first, params.second, params.third)
            if (deletedCount != 0)
                context.contentResolver.notifyChange(uri, null)
            return deletedCount
        }
    }

    override fun getType(uri: Uri?): String = when (uriMatcher.match(uri)) {
        UPCOMING_LIST_CODE, EXHIBITION_LIST_CODE -> MOVIE_LIST_CONTENT_TYPE
        UPCOMING_ITEM_CODE, EXHIBITION_ITEM_CODE -> MOVIE_ITEM_CONTENT_TYPE
        else -> throw IllegalArgumentException("Uri $uri not supported")
    }

}