package isel.pdm.moviedatabaseapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

abstract class MovieListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
    }
}
