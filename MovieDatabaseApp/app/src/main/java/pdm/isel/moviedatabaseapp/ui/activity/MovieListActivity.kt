package pdm.isel.moviedatabaseapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_movie_list.*
import pdm.isel.moviedatabaseapp.ui.adapter.MovieAdapter
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.domain.AppController
import pdm.isel.moviedatabaseapp.domain.ParametersContainer
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.exceptions.AppException

class MovieListActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override val layout: Int = R.layout.activity_movie_list
    var action: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        action = intent.getStringExtra("action")
        AppController.actionHandler(
                action,
                ParametersContainer(
                        app = (application as MovieApplication),
                        successCb = { pair -> displayMovies(pair.first!!, intent.getStringExtra("toolbarText")) },
                        errorCb = { error -> displayError(error) }
                )
        )
    }

    private fun displayMovies(movies: MovieListDto, toolbarText: String) {
        if (movies.dates != null)
            this.my_toolbar.subtitle = resources.getString(R.string.DateRange, movies.dates.minimum, movies.dates.maximum)
        this.my_toolbar.title = toolbarText

        configureAdapter(movies)
    }

    private fun configureAdapter(movies: MovieListDto) {
        val movieAdapter = MovieAdapter(
                this,
                R.layout.movie_list_entry_layout,
                movies.results.toMutableList(),
                (application as MovieApplication).imageLoader
        )

        movieListView.adapter = movieAdapter
        movieListView.emptyView = empty

        movieListView.setOnItemClickListener { parent, view, position, id ->
            AppController.actionHandler(
                    "MOVIE_DETAILS",
                    ParametersContainer(
                            app = (application as MovieApplication),
                            id = movieAdapter.getItem(position).id,
                            successCb = { pair -> sendIntent(pair.second!!) },
                            errorCb = { error -> displayError(error) },
                            source = action
                    )
            )
        }

        if (movies.page == null)
            return
        movieListView.setOnScrollListener(object : EndlessScrollListener(20, movies.page) {
            override fun onLoadMore(page: Int, totalItemsCount: Int): Boolean {
                AppController.actionHandler(
                        action,
                        ParametersContainer(
                                app = (application as MovieApplication),
                                page = page,
                                successCb = { pair -> pair.first!!.results.forEach { movieDto -> movieAdapter.add(movieDto) } },
                                errorCb = { error -> displayError(error) }
                        )
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

    private fun displayError(error: AppException) {
        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
    }
}
