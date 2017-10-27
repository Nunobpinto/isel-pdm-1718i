package pdm.isel.moviedatabaseapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_movie_details.*
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.model.MovieDto

class MovieDetailsActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override  val layout : Int = R.layout.activity_movie_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        my_toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        my_toolbar.setNavigationOnClickListener( { onBackPressed() })
        posterView.setDefaultImageResId(R.drawable.default_poster)
        val intent = intent
        val movie : MovieDto = intent.getParcelableExtra("movie")

        movieTitleView.text = movie.title
        overviewView.text = movie.overview
        rateView.text = movie.voteAverage.toString() + "/10"
        if ( movie.poster != null )
            posterView.setImageUrl(urlBuilder(movie.poster), (application as MovieApplication).imageLoader)
        releaseDateView.text = movie.releaseDate
        runtimeView.text = movie.runtime.toString() + "min"
        if ( movie.genres != null )
            genresView.text = movie.genres.map { it.name }.joinToString(", ")
    }


    private fun urlBuilder(path: String) = "http://image.tmdb.org/t/p/w185/$path?$"

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent : Intent?=null
        when(item?.itemId){
            R.id.action_about -> intent = Intent(this,ReferencesActivity::class.java)
            R.id.action_home -> intent = Intent(this,HomeActivity::class.java)
        }
        startActivity(intent!!)
        return true
    }
}
