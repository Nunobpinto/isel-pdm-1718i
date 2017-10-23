package pdm.isel.moviedatabaseapp

import android.app.ListActivity
import android.os.Bundle
import android.app.SearchManager
import android.content.Intent
import kotlinx.android.synthetic.main.activity_search_movies.*
import pdm.isel.moviedatabaseapp.service.MovieTMDBService


class SearchMoviesActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies)

        val movieService = MovieTMDBService()
        // Get the intent, verify the action and get the query
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            movieService.getMoviesByName(query, application as MovieApplication)

            //TODO: adicionar progressBar
        }
        listAdapter

    }
}
