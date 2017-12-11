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

        const val CREATE_UPCOMING =
                "create table ${MovieContentProvider.UPCOMING} ( " +
                        "${MovieContentProvider.ID} integer primary key , " +
                        "${MovieContentProvider.TITLE} boolean not null , " +
                        "${MovieContentProvider.RELEASE_DATE} real , " +
                        "${MovieContentProvider.POSTER} text , " +
                        "${MovieContentProvider.VOTE_AVERAGE} real , " +
                        "${MovieContentProvider.RUNTIME} integer , " +
                        "${MovieContentProvider.POPULARITY} real , " +
                        "${MovieContentProvider.OVERVIEW} text , " +
                        "${MovieContentProvider.GENRES} text ," +
                        "${MovieContentProvider.FOLLOWED} boolean not null)"
        const val CREATE_EXHIBITION =
                "create table ${MovieContentProvider.UPCOMING} ( " +
                        "${MovieContentProvider.ID} integer primary key , " +
                        "${MovieContentProvider.TITLE} boolean not null , " +
                        "${MovieContentProvider.RELEASE_DATE} real , " +
                        "${MovieContentProvider.POSTER} text , " +
                        "${MovieContentProvider.VOTE_AVERAGE} real , " +
                        "${MovieContentProvider.RUNTIME} integer , " +
                        "${MovieContentProvider.POPULARITY} real , " +
                        "${MovieContentProvider.OVERVIEW} text , " +
                        "${MovieContentProvider.GENRES} text)"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_EXHIBITION)
        db?.execSQL(CREATE_UPCOMING)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Alter tables if needed
    }
}
