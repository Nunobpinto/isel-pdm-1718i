package pdm.isel.moviedatabaseapp.domain.content

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieDbHelper(
        context: Context?,
        version: Int = 1, dbName: String = "MOVIE_DB"
) : SQLiteOpenHelper(
        context,
        dbName,
        null,
        version
){
    private fun createTable(db: SQLiteDatabase?, tableName: String) {
        val createCmd = "create table $tableName ( " +
                "${MovieContentProvider.ID} integer primary key , " +
                "${MovieContentProvider.TITLE} boolean not null , " +
                "${MovieContentProvider.RELEASEDATE} real , " +
                "${MovieContentProvider.POSTER} TEXT , " +
                "${MovieContentProvider.OVERVIEW} TEXT NOT NULL)"
        db?.execSQL(createCmd)
    }

    private fun dropTable(db: SQLiteDatabase?, tableName: String) =
            db?.execSQL("drop table if exists $tableName")

    override fun onCreate(db: SQLiteDatabase?) {
        createTable(db, MovieContentProvider.UPCOMING)
        createTable(db, MovieContentProvider.EXHIBITION)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) =
            throw UnsupportedOperationException("Upgrade not implemented yet!!!!")
}
