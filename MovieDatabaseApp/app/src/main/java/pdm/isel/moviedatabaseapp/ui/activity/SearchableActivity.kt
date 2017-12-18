package pdm.isel.moviedatabaseapp.ui.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_searchable.*
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.ui.adapter.MovieAdapter

class SearchableActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override val layout: Int = R.layout.activity_movie_list
    var query: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.my_toolbar.title = resources.getString(R.string.searchResults)
        val intent = intent
        if (Intent.ACTION_SEARCH.equals(intent.action)) {
            //TODO: add progress bar
            query = intent.getStringExtra(SearchManager.QUERY)
            (application as MovieApplication).remoteRepository.getMoviesByName(
                    query,
                    1,
                    application,
                    { movies, _ -> displayMovies(movies) },
                    { error -> displayError(error) }
            )
        }
    }

    private fun displayError(error: VolleyError) {
        Toast.makeText(this, R.string.errorInfo, Toast.LENGTH_LONG).show()
    }

    private fun displayMovies(movies: MovieListDto) {
        val movieAdapter = MovieAdapter(
                this,
                R.layout.movie_list_entry_layout,
                movies.results.toMutableList(),
                (application as MovieApplication).imageLoader
        )
        movieListView.adapter = movieAdapter
        movieListView.emptyView = empty

        movieListView.setOnItemClickListener { parent, view, position, id ->
            //TODO: Get movie details through repo class, instead of hardcoded request here
            (application as MovieApplication).remoteRepository.getMovieDetails(
                    movieAdapter.getItem(position).id,
                    application,
                    { movie, _ -> sendIntent(movie) },     //TODO: add similar movies, cast, etc eventually
                    { error -> displayError(error) }
            )
        }

        if (movies.page == null)
            return
        movieListView.setOnScrollListener(object : EndlessScrollListener(20, movies.page) {
            override fun onLoadMore(page: Int, totalItemsCount: Int): Boolean {
                (application as MovieApplication).remoteRepository.getMoviesByName(
                        query,
                        page,
                        application,
                        { movies, _ -> movies.results.forEach { movieDto -> movieAdapter.add(movieDto) } },
                        { error -> displayError(error) }
                )
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent: Intent? = null
        when (item?.itemId) {
            R.id.action_about -> intent = Intent(this, ReferencesActivity::class.java)
            R.id.action_home -> intent = Intent(this, HomeActivity::class.java)
            R.id.action_preferences ->  intent = Intent(this, PreferencesActivity::class.java)
        }
        startActivity(intent!!)
        return true
    }

    private fun sendIntent(movie: MovieDto) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("toolbarText", "Details of " + movie.title)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }
}
