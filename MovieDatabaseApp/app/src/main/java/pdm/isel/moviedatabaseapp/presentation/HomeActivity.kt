package pdm.isel.moviedatabaseapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_home.*
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.model.dataDto.MovieListDto

class HomeActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override  val layout : Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        my_toolbar.title = "Home"
        searchButton.setOnClickListener({
            val query: String = inputEditText.text.toString().replace(" ", "+")
            (application as MovieApplication).let {
                it.service.getMoviesByName(
                        query,
                        application,
                        {movie -> startActivity(createIntent(Intent(this, MovieListActivity::class.java), movie, "Search Results"))},
                        {volleyError -> generateErrorWarning(volleyError)})
            }
        })

        nowPlayingButton.setOnClickListener({
            (application as MovieApplication).let {
                it.service.getNowPlayingMovies(
                        application,
                        {movies->startActivity(createIntent(Intent(this, MovieListActivity::class.java), movies, "Movies Now Playing"))},
                        {volleyError -> generateErrorWarning(volleyError)})
            }
        })

        upcomingMoviesButton.setOnClickListener({
            (application as MovieApplication).let {
                it.service.getUpComingMovies(
                        application,
                        {movies->startActivity(createIntent(Intent(this, MovieListActivity::class.java), movies, "Upcoming Movies"))},
                        {volleyError -> generateErrorWarning(volleyError)})
            }
        })

        mostPopularMoviesButton.setOnClickListener({
            (application as MovieApplication).let {
                it.service.getMostPopularMovies(
                        application,
                        {movies->startActivity(createIntent(Intent(this, MovieListActivity::class.java),movies, "Most Popular Movies"))},
                        {volleyError -> generateErrorWarning(volleyError)})
            }
        })
    }

    private fun createIntent(intent: Intent, dto: MovieListDto, toolbarText: String): Intent? {
        intent.putExtra("toolbarText", toolbarText)
        intent.putExtra("results", dto)
        return intent
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent : Intent?=null
        when(item?.itemId){
            R.id.action_about -> intent = Intent(this,ReferencesActivity::class.java)
            R.id.action_home -> {
                intent = Intent(this,HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
        }
        startActivity(intent!!)
        return true
    }

    private fun generateErrorWarning(volleyError: VolleyError) {
        Toast.makeText(this,"Error getting information", Toast.LENGTH_LONG).show()
    }
}
