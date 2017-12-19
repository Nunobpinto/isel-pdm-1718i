package pdm.isel.moviedatabaseapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_movie_details.*
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.domain.model.MovieDto
import java.util.*
import android.widget.Toast
import pdm.isel.moviedatabaseapp.domain.AppController
import pdm.isel.moviedatabaseapp.domain.ParametersContainer
import pdm.isel.moviedatabaseapp.exceptions.AppException

class MovieDetailsActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override val layout: Int = R.layout.activity_movie_details
    private var movie : MovieDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        my_toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        my_toolbar.setNavigationOnClickListener({ onBackPressed() })

        if(savedInstanceState != null){
            this.movie = savedInstanceState.getParcelable("movie")
            displayMovie(movie!!)
        }
        else{
            val intent = intent
            val id: Int = intent.getIntExtra("id", 0)
            val source: String = intent.getStringExtra("source")

            AppController.actionHandler(
                    AppController.MOVIE_DETAILS,
                    ParametersContainer(
                            app = (application as MovieApplication),
                            id = id,
                            successCb = { pair -> displayMovie(pair.second!!) },
                            errorCb = { error -> displayError(error) },
                            source = source
                    )
            )
        }

    }

    private fun displayMovie(movie: MovieDto) {
        this.movie = movie
        posterView.setDefaultImageResId(R.drawable.default_poster)
        movieTitleView.text = movie.title
        overviewView.text = movie.overview
        ratingView.text = resources.getString(R.string.rating, movie.voteAverage)
        runtimeView.text = resources.getString(R.string.runtime, movie.runtime)
        releaseDateView.text = movie.releaseDate
        if(movie.poster != null)
            posterView.setImageUrl(urlBuilder(movie.poster), (application as MovieApplication).imageLoader)
        if(movie.genres != null)
            genresView.text = movie.genres.joinToString(", ") { it.name }

        configureToggleButton(movie)
    }

    private fun configureToggleButton(movie: MovieDto) {
        val date = parseCurrentDate()
        if(movie.releaseDate != null && movie.releaseDate > date) {
            (application as MovieApplication).localRepository.getFollowedMovies(
                    { movies -> toggleButton.isChecked = movies.any { it.id == movie.id } },
                    { makeToast("Error loading followed movies") }
            )
            toggleButton.setOnCheckedChangeListener({ _, isChecked ->
                if(!isChecked) {
                    (application as MovieApplication).localRepository.unfollowMovie(
                            movie.id,
                            { makeToast("Movie unfollowed!") },
                            { makeToast("Couldn't unfollow movie!") }
                    )
                } else {
                    (application as MovieApplication).localRepository.followMovie(
                            movie.id,
                            movie.title!!,
                            movie.poster!!,
                            movie.releaseDate,
                            { makeToast("Movie followed!") },
                            { makeToast("Couldn't follow the movie!") }
                    )
                }
            })
        } else
            toggleButton.visibility = View.INVISIBLE
    }

    private fun displayError(error: AppException) {
        makeToast(error.message.toString())
    }

    private fun parseCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$year-$month-$day"
    }

    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putParcelable("movie",movie)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        this.movie = savedInstanceState!!.getParcelable("movie")
    }

    private fun urlBuilder(path: String) = "http://image.tmdb.org/t/p/w185/$path?$"

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent: Intent? = null
        when(item?.itemId) {
            R.id.action_about -> intent = Intent(this, ReferencesActivity::class.java)
            R.id.action_home -> intent = Intent(this, HomeActivity::class.java)
            R.id.action_preferences -> intent = Intent(this, PreferencesActivity::class.java)
        }
        startActivity(intent!!)
        return true
    }
}
