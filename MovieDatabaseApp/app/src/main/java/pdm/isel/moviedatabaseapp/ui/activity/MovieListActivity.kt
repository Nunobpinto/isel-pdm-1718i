package pdm.isel.moviedatabaseapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_movie_list.*
import pdm.isel.moviedatabaseapp.ui.adapter.MovieAdapter
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.domain.AppController
import pdm.isel.moviedatabaseapp.domain.ParametersContainer
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.exceptions.AppException
import pdm.isel.moviedatabaseapp.exceptions.ProviderException

class MovieListActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override val layout: Int = R.layout.activity_movie_list
    var action: String = ""
    var movieAdapter: MovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        action = intent.getStringExtra("action")
        AppController.actionHandler(
                action,
                ParametersContainer(
                        app = (application as MovieApplication),
                        successCb = { movies -> displayMovies(movies, intent.getStringExtra("toolbarText")) },
                        errorCb = { error -> displayError(error) }
                )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent: Intent? = null
        when (item?.itemId) {
            R.id.action_about -> intent = Intent(this, ReferencesActivity::class.java)
            R.id.action_home -> intent = Intent(this, HomeActivity::class.java)
        }
        startActivity(intent!!)
        return true
    }

    private fun displayMovies(movies: MovieListDto, toolbarText: String) {
        if (movies.dates != null)
            this.my_toolbar.subtitle = resources.getString(R.string.from) + " " + movies.dates.minimum + " " + resources.getString(R.string.to) + " " + movies.dates.maximum
        this.my_toolbar.title = toolbarText

        movieAdapter = MovieAdapter(
                this,
                R.layout.movie_list_entry_layout,
                movies.results.toMutableList(),
                (application as MovieApplication).imageLoader
        )

        movieListView.adapter = movieAdapter
        movieListView.emptyView = empty

        movieListView.setOnItemClickListener { parent, view, position, id ->
            //TODO: melhorar esta merda
            (application as MovieApplication).movieProvider.getMovieDetails(
                    movieAdapter!!.getItem(position).id,
                    application,
                    { movie -> sendIntent(movie) },
                    { displayError(ProviderException()) })
        }

        if (movies.page == null)
            return
        movieListView.setOnScrollListener(object : EndlessScrollListener(20, movies.page) {
            override fun onLoadMore(page: Int, totalItemsCount: Int): Boolean {
                AppController.actionHandler(
                        action,
                        ParametersContainer(
                                (application as MovieApplication),
                                page,
                                { movies: MovieListDto -> movies.results.forEach { movieDto -> movieAdapter!!.add(movieDto) } },
                                { error -> displayError(error) }
                        )
                )
                return true
            }
        })
    }

    private fun sendIntent(movie: MovieDto) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("toolbarText", "Details of " + movie.title)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }

    private fun displayError(error: AppException) {
        Toast.makeText(this, R.string.errorInfo, Toast.LENGTH_LONG).show()
    }

    /*
private fun requestSimilarMovies(movie: MovieDto) {
    (application as MovieApplication).let {
        it.movieProvider.getSimilarMovies(
                movie.id,
                application,
                { movies ->
                    movie.similar = movies.results
                    startActivity(createIntent(Intent(this, MovieDetailsActivity::class.java), movie, "Details of " + movie.title))
                },
                { generateErrorWarning(VolleyError()) }
        )
    }
}
*/
}
