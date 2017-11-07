package pdm.isel.moviedatabaseapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_movie_list.*
import pdm.isel.moviedatabaseapp.view.MovieAdapter
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.model.MovieDto
import pdm.isel.moviedatabaseapp.model.MovieListDto
import pdm.isel.moviedatabaseapp.providers.MovieTMDBProvider
import kotlin.reflect.declaredFunctions

class MovieListActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override val layout: Int = R.layout.activity_movie_list
    var movieAdapter:MovieAdapter? = null

    var function : String = ""
    var query : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val movieList: MovieListDto = intent.getParcelableExtra("results")
        val toolbarText: String = intent.getStringExtra("toolbarText")
        function = intent.getStringExtra("method")
        query = intent.getStringExtra("query")

        if (movieList.dates != null)
            this.my_toolbar.subtitle = resources.getString(R.string.from) + " " + movieList.dates.minimum + " " + resources.getString(R.string.to) + " " + movieList.dates.maximum
        this.my_toolbar.title = toolbarText


        movieAdapter = MovieAdapter(this, R.layout.movie_list_entry_layout, movieList.results.toMutableList(), (application as MovieApplication).imageLoader)

        movieListView.adapter = movieAdapter
        movieListView.emptyView = empty

        movieListView.setOnItemClickListener { parent, view, position, id ->
            run {
                var movie: MovieDto = movieAdapter!!.getItem(position)
                (application as MovieApplication).let {
                    it.service.getMovieDetails(
                            movie.id,
                            application,
                            { movie -> requestSimilarMovies(movie) },
                            { generateErrorWarning(VolleyError()) })
                }

            }
        }

        movieListView.setOnScrollListener(object : EndlessScrollListener(20,movieList.page!!){
            override fun onLoadMore(page: Int, totalItemsCount: Int): Boolean {
                loadNextDataFromApi(page)
                return true
            }
        })

    }

    private fun loadNextDataFromApi(page: Int) {
        val service : MovieTMDBProvider = ((application as MovieApplication).service as MovieTMDBProvider)
        var arguments:Array<Any>? = null
        if(function=="getMoviesByName"){
            arguments = arrayOf(
                    service,
                    query,
                    application,
                    { movies:MovieListDto -> movies.results.forEach { movieDto -> movieAdapter!!.add(movieDto) }},
                    {volleyError:VolleyError -> generateErrorWarning(volleyError) },
                    page
            )
        }
        else {
            arguments =
            arrayOf(service,
                    application,
                    { movies: MovieListDto -> movies.results.forEach { movieDto -> movieAdapter!!.add(movieDto) } },
                    { volleyError: VolleyError -> generateErrorWarning(volleyError) },
                    page
            )
        }

        val fn = (application as MovieApplication).service.javaClass.kotlin.declaredFunctions.find { it.name == function }
        fn!!.call(*arguments)
    }

    private fun createIntent(intent: Intent, movie: MovieDto, s: String): Intent? {
        intent.putExtra("toolbarText", s)
        intent.putExtra("movie", movie)
        return intent
    }

    private fun requestSimilarMovies(movie: MovieDto) {
        (application as MovieApplication).let {
            it.service.getSimilarMovies(
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent: Intent? = null
        when (item?.itemId) {
            R.id.action_about -> intent = Intent(this, ReferencesActivity::class.java)
            R.id.action_home -> intent = Intent(this, HomeActivity::class.java)
        }
        startActivity(intent!!)
        return true
    }

    private fun generateErrorWarning(volleyError: VolleyError) {
        Toast.makeText(this, R.string.errorInfo, Toast.LENGTH_LONG).show()
    }

}
