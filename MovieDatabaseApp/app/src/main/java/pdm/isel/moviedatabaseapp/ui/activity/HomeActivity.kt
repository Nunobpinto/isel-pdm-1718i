package pdm.isel.moviedatabaseapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_home.*
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto

class HomeActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override val layout: Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar.visibility = View.INVISIBLE

        my_toolbar.title = resources.getString(R.string.home)
        searchButton.setOnClickListener({
            progressBar.visibility = View.VISIBLE
            val query: String = inputEditText.text.toString().replace(" ", "+")
            if (query == "") {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, R.string.non_query, Toast.LENGTH_LONG).show()
            } else {
                (application as MovieApplication).let {
                    it.provider.getMoviesByName(
                            query,
                            1,
                            application,
                            { movies ->
                                startActivity(createIntent(
                                        Intent(this, MovieListActivity::class.java),
                                        movies,
                                        resources.getString(R.string.searchResults),
                                        "getMoviesByName",
                                        query))
                            }, { volleyError -> generateErrorWarning(volleyError) })
                }
            }

        })
        //TODO: meter a ir buscar a repositorio local
        nowPlayingButton.setOnClickListener({
            progressBar.visibility = View.VISIBLE
            (application as MovieApplication).let {
                it.provider.getNowPlayingMovies(
                        1,
                        application,
                        { movies ->
                            startActivity(createIntent(
                                    Intent(this, MovieListActivity::class.java),
                                    movies, resources.getString(R.string.moviesNowPlaying),
                                    "getNowPlayingMovies",
                                    ""))
                        }, { volleyError -> generateErrorWarning(volleyError) })
            }
        })
        //TODO: meter a ir buscar a repositiorio local
        upcomingMoviesButton.setOnClickListener({
            progressBar.visibility = View.VISIBLE
            (application as MovieApplication).let {
                it.provider.getUpComingMovies(
                        1,
                        application,
                        { movies ->
                            startActivity(createIntent(
                                    Intent(this, MovieListActivity::class.java),
                                    movies,
                                    resources.getString(R.string.upcomingMoviesList),
                                    "getUpComingMovies",
                                    ""))
                        }, { volleyError -> generateErrorWarning(volleyError) })
            }
        })

        mostPopularMoviesButton.setOnClickListener({
            progressBar.visibility = View.VISIBLE
            (application as MovieApplication).let {
                it.provider.getMostPopularMovies(
                        1,
                        application,
                        { movies ->
                            startActivity(createIntent(
                                    Intent(this, MovieListActivity::class.java),
                                    movies,
                                    resources.getString(R.string.mostPopularMoviesList),
                                    "getMostPopularMovies",
                                    ""))
                        }, { volleyError -> generateErrorWarning(volleyError) })
            }
        })
    }

    private fun createIntent(intent: Intent, dto: MovieListDto, toolbarText: String, method: String, query: String): Intent? {
        intent.putExtra("toolbarText", toolbarText)
        intent.putExtra("results", dto)
        intent.putExtra("method", method)
        intent.putExtra("query", query)
        progressBar.visibility = View.INVISIBLE
        return intent
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent: Intent? = null
        when (item?.itemId) {
            R.id.action_about -> intent = Intent(this, ReferencesActivity::class.java)
            R.id.action_home -> {
                intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
        }
        startActivity(intent!!)
        return true
    }

    private fun generateErrorWarning(volleyError: VolleyError) {
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(this, R.string.errorInfo, Toast.LENGTH_LONG).show()
    }
}
