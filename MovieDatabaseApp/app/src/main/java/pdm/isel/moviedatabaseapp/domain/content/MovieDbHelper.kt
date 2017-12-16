package pdm.isel.moviedatabaseapp.domain.content

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieDbHelper(
        context: Context?,
        version: Int = 1,
        dbName: String = "MOVIE_DB"
) : SQLiteOpenHelper(
        context,
        dbName,
        null,
        version
) {

    companion object {

        const val DROP_UPCOMING =
                "drop table if exists ${MovieContentProvider.UPCOMING}"
        const val DROP_NOW_PLAYING =
                "drop table if exists ${MovieContentProvider.NOW_PLAYING}"

        const val CREATE_UPCOMING =
                "create table ${MovieContentProvider.UPCOMING} ( " +
                        "${MovieContentProvider.ID} integer primary key, " +
                        "${MovieContentProvider.MOVIE_ID} integer unique, " +
                        "${MovieContentProvider.TITLE} text not null , " +
                        "${MovieContentProvider.RELEASE_DATE} real , " +
                        "${MovieContentProvider.POSTER} text , " +
                        "${MovieContentProvider.VOTE_AVERAGE} real , " +
                        "${MovieContentProvider.RUNTIME} integer , " +
                        "${MovieContentProvider.POPULARITY} real , " +
                        "${MovieContentProvider.OVERVIEW} text , " +
                        "${MovieContentProvider.GENRES} text ," +
                        "${MovieContentProvider.FOLLOWED} integer default 0)"
        const val CREATE_NOW_PLAYING =
                "create table ${MovieContentProvider.NOW_PLAYING} ( " +
                        "${MovieContentProvider.ID} integer primary key, " +
                        "${MovieContentProvider.MOVIE_ID} integer unique, " +
                        "${MovieContentProvider.TITLE} text not null , " +
                        "${MovieContentProvider.RELEASE_DATE} real , " +
                        "${MovieContentProvider.POSTER} text , " +
                        "${MovieContentProvider.VOTE_AVERAGE} real , " +
                        "${MovieContentProvider.RUNTIME} integer , " +
                        "${MovieContentProvider.POPULARITY} real , " +
                        "${MovieContentProvider.OVERVIEW} text , " +
                        "${MovieContentProvider.GENRES} text)"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_NOW_PLAYING)
        db?.execSQL(CREATE_UPCOMING)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_NOW_PLAYING)
        db?.execSQL(DROP_UPCOMING)
        db?.execSQL(CREATE_NOW_PLAYING)
        db?.execSQL(CREATE_UPCOMING)
    }
}
